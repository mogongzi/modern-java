def caculate(n)
  numberator = 4.0
  denominator = 1.0
  operation = 1.0
  pi = 0.0

  (0..n).each do
    pi += operation * (numberator / denominator)
    denominator += 2
    operation *= -1.0 
  end
  pi
end

puts caculate(1000000)
