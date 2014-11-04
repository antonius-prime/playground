-- Module: RBTree
-- Author: Antonio Paunovic

{-# LANGUAGE RankNTypes #-}
{-# LANGUAGE KindSignatures #-}
{-# LANGUAGE FlexibleContexts #-}
module Main where

import System.Environment

import AVLTree 
import RBTree
import BSTree

-- | Prompt for inserting or deleting nodes.
prompt :: forall (a :: * -> *) b. BSTree a Int => a Int -> IO b
prompt tree = 
  do
    dumpTree tree
    putStrLn promptMsg
    input <- getLine
    prompt $ action (words input)
  where dumpTree = putStrLn . drawTree . toDataTree 
        action [toDo, value]
          | toDo == "insert" = insert (readInt value) tree
          | toDo == "delete" = delete (readInt value) tree
          | otherwise        = tree
        action _             = tree
        promptMsg = "Type in: <insert|delete> <integer>" 

-- | RBTree prompt.
promptRB :: forall b. [Int] -> IO b
promptRB nums = let tree = load empty nums :: RBTree Int
                in prompt tree

-- | AVLTree propmt.
promptAVL :: forall b. [Int] -> IO b
promptAVL nums = let tree = load empty nums :: AVLTree Int
                 in prompt tree
           
-- | Main program.
--   As CL arguments, take name of the input file and type of the tree.
--   Input file is a file consisting of space-separated integers.
main :: IO ()
main = do 
    args <- getArgs
    text <- readFile (head args)

    let nums = map readInt . words $ text
        choice = args !! 1

    case choice of
      "rb" -> promptRB nums
      "avl" -> promptAVL nums
      _     -> error "Input: <path_to_initial_configuration> <avl|rb>"

    return ()
    
-- | String to int.
readInt :: String -> Int
readInt = read
  
