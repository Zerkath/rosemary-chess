pdf(NULL)
library(readr)
library(dplyr)
library(ggplot2)
new <- read_csv("data/new.csv") %>% mutate(Description = "New changes")
old <- read_csv("data/old.csv") %>% mutate(Description = "Main branch")

combined_df <- bind_rows(new, old)

combined_df$Benchmark <- sub(
  "^rosemary.PerftBenchmark.position_",
  "",
  combined_df$Benchmark
)
print(combined_df)

combined_df <- combined_df %>% rename(stdev = "Score Error (99.9%)")

plot <- ggplot(combined_df, aes(x = Score, y = Benchmark, color = Description, shape = Description)) +
  geom_point(size = 3) +
  geom_errorbarh(aes(xmin = Score - stdev, xmax = Score + stdev), height = 0.2) +
  labs(title = "Performance (lower is better)",
       x = "Score (ms/op)",
       y = "Benchmark",
       color = "Description",
       shape = "Description") +
  scale_x_log10(n.breaks = 12) +
  theme_minimal() +
  theme(legend.position = "bottom")

ggsave("analysis.png",
  plot = plot,
  width = 3840,
  height = 2160,
  units = c("px"),
  dpi = 450,
  bg = "white"
)
