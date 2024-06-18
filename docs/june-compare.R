pdf(NULL)
library(readr)
library(dplyr)
library(ggplot2)

# Read the CSV files
df1 <- read_csv("data/june/07a21d8.csv")
df2 <- read_csv("data/june/394f3e1.csv")
df3 <- read_csv("data/june/8615fd6.csv")

# Add a column to identify the commit for each data frame
df1 <- df1 %>% mutate(Commit = "07a21d8 - No cache")
df2 <- df2 %>% mutate(Commit = "394f3e1 - Cache")
df3 <- df3 %>% mutate(Commit = "8615fd6 - Cache + Primitive")

# Combine the data frames
combined_df <- bind_rows(df1, df2, df3)
combined_df$Benchmark <- sub(
  "^rosemary.PerftBenchmark.position_",
  "",
  combined_df$Benchmark
)

combined_df <- combined_df %>% select(-Mode, -Threads, -Samples, -Unit)
combined_df <- combined_df %>% rename(stdev = "Score Error (99.9%)")

# Print the combined data frame
print(combined_df)

# Optionally, write the combined data frame to a new CSV file
write_csv(combined_df, "data/june/combined_benchmarks.csv")

plot <- ggplot(combined_df, aes(x = Score, y = Benchmark, color = Commit, shape = Commit)) +
  geom_point(size = 3) +
  geom_errorbarh(aes(xmin = Score - stdev, xmax = Score + stdev), height = 0.2) +
  labs(title = "Performance (lower is better)",
       x = "Score (ms/op)",
       y = "Benchmark",
       color = "Commit",
       shape = "Commit") +
  scale_x_log10(n.breaks = 12) +
  theme_minimal() +
  theme(legend.position = "bottom")

ggsave("june-changes-2024.png",
  plot = plot,
  width = 3840,
  height = 2160,
  units = c("px"),
  dpi = 450,
  bg = "white"
)
