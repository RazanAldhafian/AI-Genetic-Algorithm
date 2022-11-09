# Genetic Algorithm
Smart Loading Cargo Plan Using Genetic Algorithm

# The Problem
a cargo company that wants to optimize its container-loading plan.
The loading plan involves loading the shipment items, each of which weighs a different amount, into the available containers such that the weights of the items are distributed evenly between containers.
# Solution

The Solution representation will be as follows: <br> <br>
a user input population size that consists of chromosomes (each chromosome is an array of items), 
the chromosome consists of genes (each gene represents an item, an item’s value represents the container it’s assigned to).
In the beginning, the user is asked to input the number of containers, number of items and an option for the weight of items which can be either 1 or 2,
and insert the population size and mutation operator.
Once the values are input, then the chromosomes will be generated then the fitness function will be applied on them.

The Genetic algorithm is implemented as follows: 
1.	Generating an initial population of randomly generated solutions, and evaluating the fitness of every individual in the population. 
2.	Using binary tournament selection (with replacement) twice to select two parents. 
3.	Running single-point crossover on these parents to give 2 children. 
4.	Running mutation on e and f to give two new solutions. Evaluating the new solutions fitness. 
5.	Running weakest replacement.  

### Built With:

<img width="100" alt="logo" src="https://user-images.githubusercontent.com/98522684/200922593-1ef6f563-63e6-4132-a4b6-0e5200b0aa08.png">
