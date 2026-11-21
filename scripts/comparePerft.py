#!/usr/bin/env python3
# /// script
# requires-python = ">=3.12"
# dependencies = [
#     "pandas>=2.3.3",
#     "seaborn>=0.13.2",
#     "matplotlib>=3.8",
#     "numpy>=1.26",
# ]
# ///
"""Render a perft comparison chart from benchmark CSV files.

Inputs may be either raw JMH CSV output (``-rf csv``) or the older
hand-assembled combined CSVs (Benchmark, Score, stdev, Commit, Description).
Each input gets a label that is used in the chart legend/rows; by default the
label is the CSV file stem, and it can be overridden with ``--label``.

The PEP 723 metadata above lets uv install the dependencies on demand, so no
pyproject.toml or checked-in virtualenv is required.

Example:
    uv run scripts/comparePerft.py \
        --label base=main --label head=my-branch \
        /tmp/rosemary-bench/base.csv /tmp/rosemary-bench/head.csv
"""

from __future__ import annotations

import argparse
import sys
from pathlib import Path

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import seaborn as sns

BENCHMARK_PREFIX = "position_"


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Compare perft benchmark results and render a chart.",
    )
    parser.add_argument(
        "csv_files",
        nargs="+",
        type=Path,
        metavar="CSV",
        help="JMH result CSV or combined benchmark CSV",
    )
    parser.add_argument(
        "-l",
        "--label",
        action="append",
        default=[],
        metavar="NAME=LABEL",
        help="Override the label for a CSV (NAME is the file stem); repeatable",
    )
    parser.add_argument(
        "-o",
        "--output",
        type=Path,
        default=Path("analysis.png"),
        help="output image path (default: analysis.png)",
    )
    parser.add_argument(
        "--title",
        default="Chess Performance Benchmark Results (Lower is Better)",
        help="chart title",
    )
    parser.add_argument(
        "--save-data",
        type=Path,
        help="optionally write the normalized, combined data to a CSV",
    )
    return parser.parse_args()


def parse_labels(raw: list[str]) -> dict[str, str]:
    labels: dict[str, str] = {}
    for item in raw:
        name, sep, label = item.partition("=")
        if not sep or not name or not label:
            raise SystemExit(f"--label expects NAME=LABEL, got {item!r}")
        labels[Path(name).stem] = label
    return labels


def column(df: pd.DataFrame, name: str, default: str = "") -> pd.Series:
    """Return a column, or a default-valued series if it is absent."""
    if name in df.columns:
        return df[name]
    return pd.Series([default] * len(df), index=df.index)


def load_benchmarks(path: Path, label: str) -> pd.DataFrame:
    df = pd.read_csv(path)

    if "Benchmark" not in df.columns or "Score" not in df.columns:
        raise SystemExit(
            f"{path}: expected a JMH or combined benchmark CSV with "
            f"'Benchmark' and 'Score' columns, found {list(df.columns)}"
        )

    # Raw JMH output uses fully-qualified benchmark names
    # ("rosemary.PerftBenchmark.position_pawns") while combined files tend to
    # use the short form ("pawns"). Normalize both to the short form.
    benchmarks = (
        df["Benchmark"]
        .astype(str)
        .str.rsplit(".", n=1)
        .str[-1]
        .str.replace(f"^{BENCHMARK_PREFIX}", "", regex=True)
    )

    if "Description" in df.columns:
        descriptions = df["Description"].astype(str)
    else:
        descriptions = pd.Series([label] * len(df), index=df.index)

    stdev = column(df, "Score Error (99.9%)", np.nan)
    if stdev.isna().all() and "stdev" in df.columns:
        stdev = df["stdev"]

    normalized = pd.DataFrame(
        {
            "Benchmark": benchmarks,
            "Score": pd.to_numeric(df["Score"], errors="coerce"),
            "stdev": pd.to_numeric(stdev, errors="coerce").fillna(0.0),
            "Commit": column(df, "Commit", "").astype(str),
            "Description": descriptions.astype(str),
            "Unit": column(df, "Unit", "ms/op").astype(str),
        }
    )

    return normalized.dropna(subset=["Score"])


def main() -> int:
    args = parse_args()
    labels = parse_labels(args.label)

    frames = []
    for path in args.csv_files:
        if not path.is_file():
            raise SystemExit(f"{path}: no such file")
        label = labels.get(path.stem, path.stem.replace("-", " ").replace("_", " "))
        frames.append(load_benchmarks(path, label))

    data = pd.concat(frames, ignore_index=True)
    if data.empty:
        raise SystemExit("no benchmark rows found in the given CSV files")

    if args.save_data:
        data.to_csv(args.save_data, index=False)

    # --- Enhanced Styling ---
    unique_descriptions = list(dict.fromkeys(data["Description"]))
    color_palette = sns.color_palette("Set2", n_colors=len(unique_descriptions))
    color_map = dict(zip(unique_descriptions, color_palette))

    sns.set_theme(style="ticks")
    plt.rcParams["font.family"] = "sans-serif"
    plt.rcParams["font.sans-serif"] = ["Arial", "DejaVu Sans"]
    plt.rcParams["font.size"] = 10
    plt.rcParams["axes.labelsize"] = 11
    plt.rcParams["axes.titlesize"] = 12
    plt.rcParams["xtick.labelsize"] = 9
    plt.rcParams["ytick.labelsize"] = 9

    # --- Create Subplots ---
    benchmarks = data["Benchmark"].unique()
    n_benchmarks = len(benchmarks)
    n_cols = 3
    n_rows = int(np.ceil(n_benchmarks / n_cols))

    fig, axes = plt.subplots(n_rows, n_cols, figsize=(16, n_rows * 3.5), dpi=120)
    axes = axes.flatten() if n_benchmarks > 1 else [axes]

    units = data["Unit"].unique()
    x_label = f"Time ({units[0]})" if len(units) == 1 else "Time"

    # Plot each benchmark in its own subplot
    for idx, benchmark in enumerate(benchmarks):
        ax = axes[idx]
        df_bench = data[data["Benchmark"] == benchmark].sort_values("Score")

        # Create bar chart with consistent colors per description
        x_pos = np.arange(len(df_bench))
        bar_colors = [color_map[desc] for desc in df_bench["Description"]]
        ax.barh(x_pos, df_bench["Score"], color=bar_colors, alpha=0.8)

        # Add error bars separately for better styling control
        ax.errorbar(
            df_bench["Score"],
            x_pos,
            xerr=df_bench["stdev"],
            fmt="none",
            ecolor="black",
            capsize=5,
            linewidth=1.5,
            alpha=0.6,
        )

        # Customize subplot
        ax.set_yticks(x_pos)
        ax.set_yticklabels(df_bench["Description"])
        ax.set_xlabel(x_label, fontweight="bold")
        ax.set_title(benchmark.replace("_", " ").title(), fontweight="bold", pad=10)
        ax.grid(True, alpha=0.3, axis="x", linestyle="--", linewidth=0.5)
        ax.set_axisbelow(True)

        # Remove spines
        for spine in ["top", "right"]:
            ax.spines[spine].set_visible(False)

        # Set x-axis limits to start at 50% of minimum for better visibility
        min_score = df_bench["Score"].min()
        max_score = (df_bench["Score"] + df_bench["stdev"]).max()
        ax.set_xlim(left=min_score * 0.5, right=max_score)

    # Hide unused subplots
    for idx in range(n_benchmarks, len(axes)):
        axes[idx].set_visible(False)

    fig.suptitle(args.title, fontweight="bold", fontsize=16, y=0.995)

    plt.tight_layout(rect=[0, 0, 1, 0.99])
    plt.savefig(
        args.output, dpi=150, bbox_inches="tight", facecolor="white", edgecolor="none"
    )
    plt.close()

    print(f"✓ Created {n_benchmarks} benchmark charts saved as '{args.output}'")
    return 0


if __name__ == "__main__":
    sys.exit(main())
