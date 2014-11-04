-- Module: BSTree
-- Author: Antonio Paunovic

{-# LANGUAGE RankNTypes #-}
{-# LANGUAGE MultiParamTypeClasses #-}

-- | General class for the family of the binary search trees.

module BSTree ( BSTree(..), drawTree) where

import Data.Tree

-- | BSTree is parametrized by a concrete implementation of BSTree and Ord element
--   of the tree.
class (Show b, Ord b) => BSTree a b where
    toDataTree :: Show b => a b -> Tree String
    load :: a b -> [b] -> a b
    unload :: a b -> [b] -> a b
    insert :: b -> a b -> a b
    delete :: b -> a b -> a b
    empty :: a b
    
    inorder :: a b -> [b]
    preorder :: a b -> [b]
    postorder :: a b -> [b]


