
import random
import sys

SIZE = int(sys.argv[1])

randomAdjacencyMatrix = [[None for i in range(SIZE)] for j in range(SIZE)]

for i in range(len(randomAdjacencyMatrix)):
    for j in range(len(randomAdjacencyMatrix)):
        randomAdjacencyMatrix[i][j] = random.choice([True, False])
        # randomAdjacencyMatrix[j][i] = randomAdjacencyMatrix[i][j]

for i in range(len(randomAdjacencyMatrix)):
    for j in range(i, len(randomAdjacencyMatrix)):
        edge = randomAdjacencyMatrix[i][j]
        if randomAdjacencyMatrix[i][j] and i != j:
            print(f"{i}\t{j}")
