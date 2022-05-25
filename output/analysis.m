x = csvread("randomVSrandom.csv");

n = size(x,1);
m = mean(x);
stdev = std(x);
z_val = 1.960;


m
c = z_val*stdev/n