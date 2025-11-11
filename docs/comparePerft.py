import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import numpy as np
import sys

# --- Configuration ---
output_name = sys.argv[1] if len(sys.argv) > 1 else "analysis"
output_file = f"{output_name}.png"

# --- Load CSVs ---
data = pd.read_csv("combined_benchmarks.csv")

# --- Enhanced Styling ---
# Create consistent color mapping for descriptions
unique_descriptions = data["Description"].unique()
color_palette = sns.color_palette("Set2", n_colors=len(unique_descriptions))
color_map = dict(zip(unique_descriptions, color_palette))

sns.set_theme(style="ticks")
plt.rcParams['font.family'] = 'sans-serif'
plt.rcParams['font.sans-serif'] = ['Arial', 'DejaVu Sans']
plt.rcParams['font.size'] = 10
plt.rcParams['axes.labelsize'] = 11
plt.rcParams['axes.titlesize'] = 12
plt.rcParams['xtick.labelsize'] = 9
plt.rcParams['ytick.labelsize'] = 9

# --- Create Subplots ---
benchmarks = data["Benchmark"].unique()
n_benchmarks = len(benchmarks)
n_cols = 3
n_rows = int(np.ceil(n_benchmarks / n_cols))

fig, axes = plt.subplots(n_rows, n_cols, figsize=(16, n_rows * 3.5), dpi=120)
axes = axes.flatten() if n_benchmarks > 1 else [axes]

# Plot each benchmark in its own subplot
for idx, benchmark in enumerate(benchmarks):
    ax = axes[idx]
    df_bench = data[data["Benchmark"] == benchmark].sort_values("Score")
    
    # Create bar chart with consistent colors per description
    x_pos = np.arange(len(df_bench))
    bar_colors = [color_map[desc] for desc in df_bench["Description"]]
    bars = ax.barh(x_pos, df_bench["Score"], 
                   color=bar_colors, alpha=0.8)
    
    # Add error bars separately for better styling control
    ax.errorbar(df_bench["Score"], x_pos, xerr=df_bench["stdev"],
                fmt='none', ecolor='black', capsize=5, linewidth=1.5, alpha=0.6)
    
    # Customize subplot
    ax.set_yticks(x_pos)
    ax.set_yticklabels(df_bench["Description"])
    ax.set_xlabel("Time (ms/op)", fontweight='bold')
    ax.set_title(benchmark.replace('_', ' ').title(), fontweight='bold', pad=10)
    ax.grid(True, alpha=0.3, axis='x', linestyle='--', linewidth=0.5)
    ax.set_axisbelow(True)
    
    # Remove spines
    for spine in ['top', 'right']:
        ax.spines[spine].set_visible(False)
    
    # Set x-axis limits to start at 50% of minimum for better visibility
    min_score = df_bench["Score"].min()
    max_score = (df_bench["Score"] + df_bench["stdev"]).max()
    ax.set_xlim(left=min_score * 0.5, right=max_score)

# Hide unused subplots
for idx in range(n_benchmarks, len(axes)):
    axes[idx].set_visible(False)

fig.suptitle("Chess Performance Benchmark Results (Lower is Better)", 
             fontweight='bold', fontsize=16, y=0.995)

plt.tight_layout(rect=[0, 0, 1, 0.99])
plt.savefig(output_file, dpi=150, bbox_inches="tight", facecolor="white", edgecolor='none')
plt.close()

print(f"✓ Created {n_benchmarks} benchmark charts saved as '{output_file}'")
