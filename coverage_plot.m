fileID = fopen('coverage_output.txt','r');
formatSpec = '%d %f';
sizeA = [2 Inf];

A = fscanf(fileID,formatSpec,sizeA);

figure;
plot(A(2,:));
xlabel("Generations")
ylabel("Non-0 cells")
