-- Module: RBTree
-- Author: Antonio Paunovic

{-# LANGUAGE InstanceSigs #-}
{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE MultiParamTypeClasses #-}

module RBTree where

import Data.Tree --used for printing.
import BSTree

data Color = Red | Black deriving (Show, Eq)

-- | Data type for the tree. 
--   RBTree can be node N with red R xor black color B, left subtree, element and right subtree
--   or it can be leaf L.
data RBTree a = N Color (RBTree a) a (RBTree a) | L deriving (Show) 

-- | AVLTree is a BSTree. 
instance (Show a, Ord a) => BSTree RBTree a where

    toDataTree :: Show a => RBTree a -> Tree String
    toDataTree = toDataTree'

    load t [] = t
    load t (x:xs) = load (insert x t) xs

    unload t [] = t
    unload t (x:xs) = unload (delete x t) xs

    insert = insert'
    delete = delete'

    empty = empty'
    
    inorder   = inorder'
    preorder  = preorder'
    postorder = postorder'

--------------------------------------------------------------------------------
-- | Empty tree constructor.
empty' :: Ord a => RBTree a
empty' = L

-- | Insertion method for the RB tree data structure. 
insert' :: Ord a => a -> RBTree a -> RBTree a
insert' val tree =  
  let ins L = N Red L val L
      ins tree@(N Black left currVal right) 
          | val < currVal = balance (ins left) currVal right 
          | val > currVal = balance left currVal (ins right)
          | otherwise     = tree
      ins tree@(N Red left currVal right)
          | val < currVal = N Red (ins left) currVal right 
          | val > currVal = N Red left currVal (ins right)
          | otherwise     = tree
      N _ l v r = ins tree
  in  N Black l v r
  
-- | Balancing the tree. Different possible casses are determined by patterns
--   and transforem to balanced versions.
balance :: Ord a => RBTree a -> a -> RBTree a -> RBTree a
balance (N Red ll a lr) val (N Red rl b rr) = 
  N Red (N Black ll a lr) val (N Black rl b rr) 

balance (N Red (N Red lll a llr) b lr) val r = 
  N Red (N Black lll a llr) b (N Black lr val r)
  
balance (N Red ll a (N Red lrl b lrr)) val r = 
  N Red (N Black ll a lrl) b (N Black lrr val r)
  
balance l val (N Red rl a (N Red rrl b rrr)) =
  N Red (N Black l val rl) a (N Black rrl b rrr)
  
balance l val (N Red (N Red rll a rlr) b rr) =
  N Red (N Black l val rll) a (N Black rlr b rr)

balance l val r = N Black l val r

-- | Function which deletes the given element from the tree if it exists.
-- When deleting, we merge the two trees together. Invariant may be violated
-- when deleting forom a black tree.
delete' :: Ord a => a -> RBTree a -> RBTree a
delete' val tree = blacken (del tree)
  where del L = L
        del (N _ a curr b)
          | val < curr = delLeft a curr b
          | val > curr = delRight a curr b
          | otherwise  = merge a b
        delLeft a@(N Black _ _ _) y b = balLeft (del a) y b -- height change -> rebalance
        delLeft a y b = N Red (del a) y b
        delRight a y b@(N Black _ _ _) = balRight a y (del b)
        delRight a y b = N Red a y (del b) 

-- | Deletition balancing, complicated...
balLeft :: Ord a => RBTree a -> a -> RBTree a -> RBTree a
balLeft (N Red a x b) y c = N Red (N Black a x b) y c
balLeft bl x (N Black a y b) = balance bl x (N Red a y b)
balLeft bl x (N Red (N Black a y b) z c) = N Red (N Black bl x a) y (balance b z (sub1 c))

balRight :: Ord a => RBTree a -> a -> RBTree a -> RBTree a
balRight a x (N Red b y c) = N Red a x (N Black b y c)
balRight (N Black a x b) y bl = balance (N Red a x b) y bl
balRight (N Red a x (N Black b y c)) z bl = N Red (balance (sub1 a) x b) y (N Black c z bl)

-- | Change the color of the root.
sub1 :: RBTree a -> RBTree a
sub1 (N Black a x b) = N Red a x b
sub1 _ = error "invariance violation"

-- | Merge the structure on deletitions.
merge :: Ord a => RBTree a -> RBTree a -> RBTree a
merge L x = x
merge x L = x
merge (N Red a x b) (N Red c y d) =
  case merge b c of
    N Red b' z c' -> N Red (N Red a x b') z (N Red c' y d)
    bc -> N Red a x (N Red bc y d)
merge (N Black a x b) (N Black c y d) =
  case merge b c of
    N Red b' z c' -> N Red (N Black a x b') z (N Black c' y d)
    bc -> balLeft a x (N Black bc y d)
merge a (N Red b x c) = N Red (merge a b) x c
merge (N Red a x b) c = N Red a x (merge b c)

-- | Blacken the node.
blacken :: RBTree a -> RBTree a
blacken L = L
blacken (N _ l v r) = N Black l v r

member :: (Eq a) => a -> RBTree a -> Bool
member _ L = False
member val (N _ left currVal right) 
  = val == currVal || member val left || member val right

-- | Size of the tree.  
size :: RBTree a -> Int
size L = 0
size (N _ l _ r) = 1 + size l + size r

-- | Make the inorder tree traversal and store its result in the list.
inorder' :: Ord a => RBTree a -> [a]
inorder' L = []
inorder' (N _ l x r) = inorder' l ++ [x] ++ inorder' r

-- | Make the preorder tree traversal and store its result in the list.
preorder' :: Ord a => RBTree a -> [a]
preorder' L = []
preorder' (N _ l x r) = [x] ++ preorder' l ++ preorder' r

-- | Make the postorder tree traversal and store its result in the list.
postorder' :: Ord a => RBTree a -> [a]
postorder' L = []
postorder' (N _ l x r) = postorder' l ++ postorder' r ++ [x]

--------------------------------------------------------------------------------
-- | Transform the tree to the rose tree so it can be printed.
toDataTree' :: Show a =>  RBTree a -> Tree String
toDataTree' L = Node (black "null") []
toDataTree' (N c L v L) = 
  case c of
    Red  -> Node (red (show v)) []
    Black -> Node (black (show v)) []
toDataTree' (N c L v r) = 
  case c of
    Red  -> Node (red (show v)) [toDataTree' r]
    Black -> Node (black (show v)) [toDataTree' r]

toDataTree' (N c l v L) = 
  case c of
    Red  -> Node (red (show v)) [toDataTree' l]
    Black -> Node (black (show v)) [toDataTree' l]


toDataTree' (N c l v r) = 
  case c of
    Red  -> Node (red (show v)) [toDataTree' l, toDataTree' r]
    Black -> Node (black (show v)) [toDataTree' l, toDataTree' r]

--------------------------------------------------------------------------------
-- Colors, visible in posix compatabile terminals.
type ANSIColor = String

black :: String -> ANSIColor
black str = "\x1b[40m" ++ str ++ blank

red :: String -> ANSIColor
red str = "\x1b[41m" ++ str ++ blank

blank :: ANSIColor
blank = "\x1b[0m"
