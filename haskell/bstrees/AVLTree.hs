-- Module: AVLTree
-- Author: Antonio Paunovic

{-# LANGUAGE MultiParamTypeClasses #-}
{-# LANGUAGE InstanceSigs #-}
{-# LANGUAGE FlexibleInstances #-}

module AVLTree (AVLTree (..)) where

import BSTree
import Data.Tree -- used for printing.

-- | Data type for the tree.
--   AVLTree can be node N with left subtree, element and right subtree
--   or it can be leaf L.
data AVLTree a = N (AVLTree a) a (AVLTree a) | L deriving (Show)

-- | AVLTree is a BSTree. 
instance (Show a, Ord a) => BSTree AVLTree a where
    
    toDataTree :: Show a => AVLTree a -> Tree String
    toDataTree L = Node "x" []
    toDataTree (N l v r) = Node (show v) [toDataTree l, toDataTree r]

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
empty' :: Ord a => AVLTree a
empty' = L

-- | Insertion method for the AVL tree data structure. 
insert' :: Ord a => a -> AVLTree a -> AVLTree a
insert' val tree  =
  let ins L = N L val L
      ins (N left currVal right) 
        | val == currVal = N left currVal right
        | val < currVal && bf (ins left) right > 1 && val < value left   = balanceLL $ N (ins left) currVal right
        | val < currVal && bf (ins left) right > 1 && val > value left   = balanceLR $ N (ins left) currVal right
        | val > currVal && bf left (ins right) < -1 && val < value right = balanceRL $ N left currVal (ins right)
        | val > currVal && bf left (ins right) < -1 && val > value right = balanceRR $ N left currVal (ins right)
        | val < currVal = N (ins left) currVal right
        | val > currVal = N left currVal (ins right)
  in ins tree
  
-- | Delete method for the AVL tree data structure.
delete' :: Ord a => a -> AVLTree a -> AVLTree a

delete' _ L = L

delete' val (N L currVal L)
    | val == currVal = L
    | otherwise      = N L currVal L

delete' val (N left currVal L)
    | val == currVal = left
    | otherwise      = N (delete' val left) currVal L

delete' val (N L currVal right)
    | val == currVal = right
    | otherwise      = N L currVal (delete' val right)

delete' val (N left currVal right) 
    | val == currVal                                 = N left inSucc (delete' inSucc right)
    | val < currVal && abs (bf delLeft right) < 2    = N delLeft currVal right
    | val > currVal && abs (bf left delRight) < 2    = N left currVal delRight
    | val < currVal && bf (lc right) (rc right) < 0  = balanceRR $ N delLeft currVal right
    | val > currVal && bf (lc left) (rc left) > 0    = balanceLL $ N left currVal delRight
    | val < currVal                                  = balanceRL $ N delLeft currVal right
    | val > currVal                                  = balanceLR $ N left currVal delRight
  where inSucc = inorderSucc right
        delLeft = delete' val left
        delRight = delete' val right

-- | Balancing the tree. Different possible casses are determined by patterns
--   and transforem to balanced versions.
rightRotate :: AVLTree a -> AVLTree a
rightRotate (N (N ll x lr) y r) = N ll x (N lr y r)

leftRotate :: AVLTree a -> AVLTree a
leftRotate (N l x (N rl y rr)) = N (N l x rl) y rr

balanceLL :: Ord a => AVLTree a -> AVLTree a
balanceLL zRot@(N (N (N lll x llr) y lr) z r ) = rightRotate zRot

balanceLR :: Ord a => AVLTree a -> AVLTree a
balanceLR (N yRot@(N ll y (N lrl x lrr)) z r) = 
  rightRotate $ N (leftRotate yRot) z r
  
balanceRR :: Ord a => AVLTree a -> AVLTree a
balanceRR zRot@(N l z (N rl y (N rrl x rrr))) = leftRotate zRot

balanceRL :: Ord a => AVLTree a -> AVLTree a
balanceRL (N l z yRot@(N (N rll x rlr) y rr)) = 
  leftRotate $ N l z (rightRotate yRot) 

-- | Compute the height of the tree.
height :: Ord a => AVLTree a -> Int
height L = 0
height (N left _ right) = 1 + max (height left) (height right)

-- | Get value from the node.
value :: AVLTree a -> a
value (N _ val _) = val

-- | Compute the balance factor of the curren tree.
bf :: Ord a => AVLTree a -> AVLTree a -> Int
bf left right = height left - height right

-- | Return the left child tree of the current node .
lc :: Ord a => AVLTree a -> AVLTree a
lc (N l _ _) = l

-- | Return the right child tree of the current node .
rc :: Ord a => AVLTree a -> AVLTree a
rc (N _ _ r) = r

-- | Make the inorder tree traversal and store its result in the list.
inorder' :: Ord a => AVLTree a -> [a]
inorder' L = []
inorder' (N left val right) = inorder' left ++ [val] ++ inorder' right

-- | Make the preorder tree traversal and store its result in the list.
preorder' :: Ord a => AVLTree a -> [a]
preorder' L = []
preorder' (N left val right) = [val] ++ preorder' left ++ preorder' right

-- | Make the postorder tree traversal and store its result in the list.
postorder' :: Ord a => AVLTree a -> [a]
postorder' L = []
postorder' (N left val right) = postorder' left ++ postorder' right ++ [val]

-- | First successor of a noe in a inorder tree traversal
inorderSucc :: Ord a => AVLTree a -> a
inorderSucc = head . inorder'

-- | Check if the AVL tree is balanced.
isBalanced :: Ord a => AVLTree a -> Bool
isBalanced L                = True
isBalanced (N left _ right) = isBalanced left && isBalanced right && abs (bf left right) < 2

