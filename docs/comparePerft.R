pdf(NULL)
library(readr)
library(ggplot2)
data <- read_csv("data/perft.csv")

df_diff <- aggregate(
  Score ~ Benchmark,
  data = data,
  FUN = function(x) {
    -diff(x)[1]
  }
)

df_diff <- df_diff[order(-df_diff$Score), ]
df_diff$Benchmark <- factor(df_diff$Benchmark, levels = df_diff$Benchmark)

print(df_diff)

ggplot(df_diff, aes(x = Benchmark, y = Score, fill = Score)) +
  geom_bar(stat = "identity", position = "dodge") +
  labs(title = "Performance Difference between New and Old Benchmarks",
       x = "Benchmark", y = "Score Difference (ms/op)") +
  theme_minimal() +
  theme(axis.text.x = element_text(angle = 45, hjust = 1))

ggsave("analysis.jpg",
  width = 2560,
  height = 1440,
  units = c("px"),
  dpi = 200,
  bg = "white"
)
