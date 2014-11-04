module Main where

import System.Random
import System.Environment
import Control.Concurrent

type Board = [[Char]]

rule2, rule13, rule4 :: Board -> (Int, Int) -> Bool
rule2 board pos = rule board pos (\x -> x == 2 || x == 3)
rule13 board pos = rule board pos (\x -> x > 3 || x < 2)
rule4 board pos = rule board pos (== 3)

rule :: Board -> (Int, Int) -> (Int -> Bool) -> Bool
rule board pos p = p (length neighbours) 
  where neighbours = filter (=='o') $  neighborhood board pos

createBoard :: Int -> Int -> [(Int, Int)] -> Board
createBoard n m living = [[if (i,j) `elem` living
                           then 'o'
                           else '.'
                           | j <- [0..m-1]] | i <- [0..n-1]]

showBoard :: Board -> IO ()
showBoard = mapM_ putStrLn 

neighborhood :: Board -> (Int, Int) -> [Char]
neighborhood board (i, j) 
  | i == 0 && j == 0 = [five, seven, eight]
  | i == 0 && j == m = [four, six, seven]
  | i == n && j == 0 = [two, three, five]
  | i == n && j == m = [one, two, four]
  | j == 0           = [two, three, five, seven, eight]
  | j == m           = [one, two, four, six, seven]
  | i == 0           = [four, five, six, seven, eight]
  | i == n           = [one, two, three, four, five]
  | otherwise        = [one, two, three, four, five, six, seven, eight]
  where n     = length board - 1
        m     = length (head board) - 1
        one   = get (i-1) (j-1)
        two   = get (i-1) j
        three = get (i-1) (j+1)
        four  = get i (j-1)
        five  = get i (j+1)
        six   = get (i+1) (j-1)
        seven = get (i+1) j
        eight = get (i+1) (j+1)
        get idx idy  = board !! idx !! idy
        
theGame :: Board -> Board
theGame b = 
  [[cond cell (i,j) | j <- [0..m-1], let cell=get i j]
  | i <- [0..n-1]]
  where n = length b
        m = length (head b)
        get idx idy  = b !! idx !! idy
        cond c (i,j)
          | c == 'o' && rule2 b (i,j) = 'o'
          | c == '.' && rule4 b (i,j) = 'o'
          | otherwise                 = '.'

world :: Int -> Int -> Int -> IO Board
world h w cnt = do
  xs <- randlist h cnt
  ys <- randlist w cnt
  let living = zip xs ys
      newworld  = createBoard h w living
  return newworld

play :: Board -> IO () 
play w = do let step = theGame w 
            showBoard step
            putStrLn ""
            threadDelay $ 10 * 100000
            play step

randlist :: Int -> Int -> IO [Int]
randlist m howmuch= do
  g <- newStdGen
  return . take howmuch $ randomRs (0, m) g

main :: IO ()
main = do
  args <- getArgs
  let w = readInt $ args !! 0
      h = readInt $ args !! 1
      n = readInt $ args !! 2
  world w h n >>= play

readInt :: String -> Int
readInt = read
