# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


from util import manhattanDistance
from game import Directions
import random, util

from game import Agent

class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """


    def getAction(self, gameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]
        
        "*** YOUR CODE HERE ***"
        
        newGhostPositions = successorGameState.getGhostPositions    # haamujen tulevat sijainnit
        newFoodList = newFood.asList()  # ruuat listaksi
        foodVal = float("inf")    
        
        for ghost in newGhostPositions():   # joka haamun uusi sijainti
            if (manhattanDistance(newPos, ghost) < 3):  
                return float("-inf")    # -ääretön, jos haamu lähellä
        for food in newFoodList:
            foodVal = min(foodVal, manhattanDistance(newPos, food)) # etsi pienin etäisyys ruokaan
        return successorGameState.getScore() + 1/foodVal

def scoreEvaluationFunction(currentGameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """

    def getAction(self, gameState):
        """
        Returns the minimax action from the current gameState using self.depth
        and self.evaluationFunction.

        Here are some method calls that might be useful when implementing minimax.

        gameState.getLegalActions(agentIndex):
        Returns a list of legal actions for an agent
        agentIndex=0 means Pacman, ghosts are >= 1

        gameState.generateSuccessor(agentIndex, action):
        Returns the successor game state after an agent takes an action

        gameState.getNumAgents():
        Returns the total number of agents in the game

        gameState.isWin():
        Returns whether or not the game state is a winning state

        gameState.isLose():
        Returns whether or not the game state is a losing state
        """
        "*** YOUR CODE HERE ***"
        actions = gameState.getLegalActions(0)  # pacmanin mahdolliset liikkeet
        bestAction = max(actions, key=lambda x: self.minimaxSearch(gameState.generateSuccessor(0, x), 1)) # paras liike
        return bestAction 
        
    def minimaxSearch(self, gameState, turn):
        evals = []
        numOfAgents = gameState.getNumAgents()  # agenttien lkm
        agentIndex = turn % numOfAgents     # kyseinen agentti
        depth = turn // numOfAgents     # haun syvyys
        if gameState.isWin() or gameState.isLose() or depth == self.depth:      
            return self.evaluationFunction(gameState)
        actions = gameState.getLegalActions(agentIndex)
        for action in actions:
            evals.append(self.minimaxSearch(gameState.generateSuccessor(agentIndex, action), turn + 1))
        if agentIndex > 0:  
            return min(evals)
        else: 
            return max(evals)

class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """
        "*** YOUR CODE HERE ***"
        actions = gameState.getLegalActions(0)
        alpha = float("-inf") 
        beta = float("inf")
        vals = []
        for action in actions:
            val = self.alphabetaSearch(gameState.generateSuccessor(0, action), 1, alpha, beta)
            alpha = max(alpha, val)
            vals.append(val)
        for i in range(len(actions)):
            if alpha == vals[i]:
                return actions[i]

    def alphabetaSearch(self, gameState, turn, alpha, beta):
        numOfAgents = gameState.getNumAgents()
        agentIndex = turn % numOfAgents
        depth = turn // numOfAgents
        if gameState.isWin() or gameState.isLose() or depth == self.depth:
            return self.evaluationFunction(gameState)
        actions = gameState.getLegalActions(agentIndex)
        if agentIndex == 0: 
            val = float("-inf")
        else: 
            val = float("inf")
        for action in actions:
            successor = gameState.generateSuccessor(agentIndex, action)
            if agentIndex > 0:
                val = min(val, self.alphabetaSearch(successor, turn + 1, alpha, beta))
                if val < alpha: 
                    return val
                else: 
                    beta = min(beta, val)
            else: 
                val = max(val, self.alphabetaSearch(successor, turn + 1, alpha, beta))
                if val > beta: 
                    return val
                else: 
                    alpha = max(alpha, val)
        return val

class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState):
        """
        Returns the expectimax action using self.depth and self.evaluationFunction

        All ghosts should be modeled as choosing uniformly at random from their
        legal moves.
        """
        "*** YOUR CODE HERE ***"
        actions = gameState.getLegalActions(0)
        return max(actions, key=lambda x: self.expectimaxSearch(gameState.generateSuccessor(0, x), 1))

    def expectimaxSearch(self, gameState, turn):
        evals = []
        numOfAgents = gameState.getNumAgents()
        agentIndex = turn % numOfAgents
        depth = turn // numOfAgents
        if gameState.isWin() or gameState.isLose() or depth == self.depth:
            return self.evaluationFunction(gameState)
        actions = gameState.getLegalActions(agentIndex) 
        for action in actions:
            evals.append(self.expectimaxSearch(gameState.generateSuccessor(agentIndex, action), turn+1))
        if agentIndex > 0:
            return sum(evals) * 1/len(evals)
        return max(evals)

def betterEvaluationFunction(currentGameState):
    """
    Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
    evaluation function (question 5).

    DESCRIPTION:
    Tarkistetaan onko tila voitto/häviö -> niille äärettömän suuri/pieni arvo
    Etsitään lähin ruoka
    Tutkitaan vaaran aste katsomalla montako haamua lähellä
    Katsotaan ovatko haamut aktiivisia
    Varmistetaan if-else ehdolla ettei jaeta nollalla
    Pyritään pois vaarasta
    
    Rehellisesti en ihan ymmärrä miksi funktio toimii niin hyvin, 
    kokeilin vain erilaisia paluuarvoja ja niiden variaatioita ja tällä
    sain parhaat tulokset
    """
    "*** YOUR CODE HERE ***"    
    foodList = currentGameState.getFood().asList()
    ghostStates = currentGameState.getGhostStates()
    pacmanPostion = currentGameState.getPacmanPosition()
    bestFood = float("inf")
    activeGhosts = 0
    danger = 0
    
    if currentGameState.isLose(): 
        return float("-inf")
    if currentGameState.isWin():  
        return float("inf") 
    for food in foodList:
        nextFood = manhattanDistance(food, pacmanPostion)
        if nextFood < bestFood:
            bestFood = nextFood
    for ghost in ghostStates:
        if manhattanDistance(ghost.getPosition(), pacmanPostion) < 3:
            danger+=1
        if ghost.scaredTimer == 0:
            activeGhosts+=1
    if activeGhosts > 0:
        return currentGameState.getScore() + 1/bestFood - danger + 1/activeGhosts
    else:
        return currentGameState.getScore() + 1/bestFood - danger + 10
    

# Abbreviation
better = betterEvaluationFunction
