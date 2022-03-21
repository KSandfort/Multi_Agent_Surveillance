figure;

filenameBug = 'coverage_output_Bug.txt';
filenameRandom = 'coverage_output_Random.txt'

if isfile(filenameBug) 
    fileID = fopen(filenameBug,'r');
    formatSpec = '%d %f';
    sizeA = [2 Inf];
    
    R = fscanf(fileID,formatSpec,sizeA);

    plot(R(2,:),'DisplayName','Bug agent');
    hold on
end 

if isfile(filenameRandom) 
    fileID = fopen(filenameRandom,'r');
    formatSpec = '%d %f';
    sizeA = [2 Inf];
    
    R = fscanf(fileID,formatSpec,sizeA);

    plot(R(2,:),'DisplayName','Random agent');
end

xlabel("Step")
ylabel("Coverage percentage")
legend()
