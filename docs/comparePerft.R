pdf(NULL)
library(readr)
library(dplyr)
library(ggplot2)
new <- read_csv("data/new.csv")
old <- read_csv("data/old.csv")

merged_df <- merge(new, old, by = "Benchmark", suffixes = c("_new", "_old"))

# Calculate the difference in scores
merged_df <- merged_df %>%
  mutate(Score_Diff = Score_new - Score_old) %>%
  select(Benchmark, Score_Diff)

merged_df$Benchmark <- sub("^rosemary.PerftBenchmark.position_", "", merged_df$Benchmark)
merged_df <- merged_df[order(-merged_df$Score_Diff), ]
merged_df$Benchmark <- factor(merged_df$Benchmark, levels = merged_df$Benchmark)
print(merged_df)

# Plot the differences using ggplot2
ggplot(merged_df, aes(x = Benchmark, y = Score_Diff, fill = Score_Diff)) +
  geom_bar(stat = "identity", position = "dodge") +
  labs(title = "Performance Difference between New and Old Benchmarks",
       x = "Benchmark", y = "Score Difference (ms/op)") +
  theme_minimal() +
  theme(axis.text.x = element_text(angle = 45, hjust = 1))

ggsave("analysis.png",
  width = 2560,
  height = 1440,
  units = c("px"),
  dpi = 200,
  bg = "white"
)
