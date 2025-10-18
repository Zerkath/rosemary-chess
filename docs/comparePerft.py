import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import numpy as np

# --- Load CSVs ---
data = pd.read_csv("combined_benchmarks.csv")
# --- Plot ---
sns.set_theme(style="whitegrid")

plt.figure(figsize=(19.2, 10.8))  # ~3840x2160 pixels at 200 dpi
num_categories = len(data["Description"].unique())
offset = 0.15

for i, (desc, df_sub) in enumerate(data.groupby("Description")):
    y_positions = df_sub["Benchmark"].astype('category').cat.codes + (i - (num_categories-1)/2) * offset
    plt.errorbar(
        df_sub["Score"],
        y_positions,
        xerr=df_sub["stdev"],
        fmt="o",
        label=desc,
        capsize=4,
        markersize=6
    )

plt.yticks(np.arange(len(data["Benchmark"].unique())), data["Benchmark"].unique())
plt.xscale("log")
plt.xlabel("Score (ms/op)")
plt.ylabel("Benchmark")
plt.title("Performance (lower is better)")
plt.legend(title="Description", loc="lower center", bbox_to_anchor=(0.5, -0.25), ncol=2)
plt.tight_layout()

plt.savefig("analysis.png", dpi=100, bbox_inches="tight", facecolor="white")
plt.close()
