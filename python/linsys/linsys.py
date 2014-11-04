# -*- coding: utf-8 -*-

from copy import deepcopy
import array as ar

#
def solveFromFiles(file_A, file_b):
    """
    First argument is the file configuration of the system matrix.
    Second argument is the file configaration of the solution vector.
    """
    A = Matrix.fromFile(file_A)
    b = MAtrix.fromFile(file_b)

    solve(A, b)

def solve(A, b, precision=1e-12):
    """
    Solve the given linear system. Try both LU and LUP decompositions.
    """

    try:
        print 'Rješenje danog sustava je:'
        x = Matrix.luSolve(A, b, precision);
    except:
        print 'Dani sustav se ne može riješiti LU dekompozicijom.'

    try:
        print 'Rješenje danog sustava je:'
        x = Matrix.lupSolve(A, b, precision)
        print A*x
    except:
        print 'Dani sustav se ne moze riješiti LUP dekompozicijom.'  
    
# 1. 
# Svejedno radi iz nekog razloga.

# 2.
def solve2(precision=1e-12):
    """
    Solution to the second exercise from the lab assignment.
    """

    A = Matrix([[3,9,6],[4,12,12],[1,-1,1]])
    b = ~Matrix([[12,12,1]])

    return solve(A, b, precision)

# 3.
# Po definiciji, na svaku se kvadratnu matricu može aplicirati LUP dekompozicija.
# Međutim, dobiveni rezultati često djeluju besmisleno. 

def solve3(precision=1e-12):
    """
    Solution to the third exercise from the lab assignment.
    """

    A = Matrix([[1,2,3],[4,5,6],[7,8,9]])
    b = ~Matrix([[4,12,12]])
    
    return solve(A, b, precision)

# 4.
# Rješenja se značajno razlikuju već u drugoj decimali.
def solve4(precision=1e-12):
    """
    Solution to the fourth exercise from the lab assignment.
    """

    A = Matrix([[1e-6,3e6,2e6],[1e6,2e6,3e6],[2e6,1e6,2e6]])
    b = ~Matrix([[12000000.000001,140000000,100000000]])

    return solve(A, b, precision)

# 5.
# Rješenje ispada točno za LUP?
def solve5(precision=1e-12):
    """
    Solution to the fifth exercise from the lab assignment.
    """

    A = Matrix([[0,1,2],[2,0,3],[3,5,1]])
    b = ~Matrix([[6,9,3]])

    return solve(A, b, precision)

# 6.
# Radi normalno za i za LU i LIP.
def solve6(precision=1e-12):
    """
    Solution to the sixth exercise from the lab assignment.
    """

    A = Matrix([[4e9,1e9,3e9],[4,2,7],[3e-9,5e-9,2e-9]])
    b = ~Matrix([[9e9,15,15e-9]])

    return solve(A, b, precision)

class Matrix:
    """ This class implements a matrix. """
    def __init__(self, list2d):
        
        self.n = len(list2d)
        self.m = len(list2d[0])
        self.shape = (self.n, self.m)

        self.matrix = []
        
        for row in list2d:
            self.matrix.append(ar.array('d', row))

        self.n = len(self.matrix)
        self.m = len(self.matrix[0])
        self.shape = (self.n, self.m)
        
    def __getitem__(self, index):
        return self.matrix[index]

    def __neg__(self):
        return Matrix.traverseUnary(lambda x: -x, self)
        
    def __add__(self, other):
        """Add to matrix. Number or other matrix can be added."""

        def addNumber(number):
            """Increment every element of matrix by number."""
            return Matrix.traverseUnary(lambda x: x+number, self)

                
        def addMatrix(other):
            """Add elements at corresponding positions of two matrices."""
            return Matrix.traverseBinary(lambda (x,y): x+y, zip, self, other)

        try:
            if isinstance(other, Matrix):
                return addMatrix(other)
            else:
                return addNumber(other)
        except:
            raise TypeError('Incompatibile operand type: for + operation')#hm -?

    def __sub__(self, other):
        return self + (-other)

    def __mul__(self, other):
        """ Multiply matrix with scalar or other matrix. """
        
        def mulNumber(number):
            """Increment every element of matrix by number."""
            return Matrix.traverseUnary(lambda x: x*number, self)

                
        def mulMatrix(other):
            """Add elements at corresponding positions of two matrices."""
            
            if self.m != other.n:
                raise Exception("[mul] uncompatabile dimensions.")

            matrix = Matrix.initMatrix(self.n, other.m)

            for i in range(0,self.n):
                for j in range(0,other.m):
                    for k in  range(0,other.n):
                        matrix[i][j] += self[i][k] * other[k][j]

            return matrix # try strassen

        try:
            if isinstance(other, Matrix):
                return mulMatrix(other)
            else:
                return mulNumber(other)
        except:
            raise TypeError('Incompatibile operand type: for * operation')#hm -?

    def __div__(self, other):

        def divNumber(number):
            """Increment every element of matrix by number."""
            return Matrix.traverseUnary(lambda x: x*(1./number), self)

                
        def divMatrix(other):
            """Add elements at corresponding positions of two matrices."""
            print 'not yet'

        try:
            if isinstance(other, Matrix):
                return divMatrix(other)
            else:
                return divNumber(other)
        except:
            raise TypeError('Incompatibile operand type: for / operation')#hm -?

    def __radd__(self, other):
        return self+other

    def __rsub__(self, other):
        return self-other

    def __rmul__(self, other):
        return self*other

    def __rdiv__(self, other):
        return self / other

    def __cmp__(self, other):
        return all(map(lambda (x,y): x!=y, zip(self,other))) # 0,1? itertools?

    def __invert__(self):
        """
        Returns a new matrix which is transposition of current matrix.
        """
        matrix = Matrix.initMatrix(self.m, self.n)

        for i in range(0,self.n):
            for j in range(0,self.m):            
                matrix[j][i] = self[i][j]

        return matrix
    
    def __str__(self):
        """Turn matrix to it's printing form."""
        strmatrix = []
        for row in self.matrix:
            strmatrix.append("\t".join([str(el) for el in row]))

        return "\n".join(strmatrix) + '\n'

    def __repr__(self):
        return str(self)

    @staticmethod
    def traverseUnary(func, matrix):
        generated = []
        
        for i in range(0, matrix.n):
            row = ar.array('d',
                [func(el) for el in matrix[i]])
            generated.append(row)

        return Matrix(generated)

    @staticmethod
    def traverseBinary(applyfunc, unifyfunc, left, right):
        matrix = []
        for i in range(0, left.n):
            row = ar.array('d',
                [applyfunc(memb) for memb in unifyfunc(left[i], right[i])])
            matrix.append(row)
            
        return Matrix(matrix)
            
    @staticmethod
    def initMatrix(numRow, numCol, initVal=0):
        matrix = []
        
        for row in range(0, numRow):
            matrix.append(ar.array('d', (initVal,)*numCol))

        return Matrix(matrix)

    @staticmethod
    def eye(n,m):
        """Create identity matrix using given parameters as dimensions."""
        matrix = Matrix.initMatrix(n,m)
        for (i,j) in zip(range(n),range(m)):
            matrix[i][j] = 1

        return matrix
                         
    @staticmethod
    def fromFile(filename):
        """Reads a text file of a specified format from wich it can extract matrix ocnfiguration."""
        import re
             
        f = open(filename, 'r')
        
        strMatrix = []
        for line in f:
            nums = re.split(r'\t*\s*',line.strip())
            strMatrix.append([float(el) for el in nums])

        return Matrix(strMatrix)

    @staticmethod
    def forwardSubstitution(L, b):

        if L.n != b.n:
            raise Exception("Uncompatabile matrices.")
        
        y = Matrix.initMatrix(b.n, b.m)
        n = b.n

        for i in range(n):
            a = b[i][0]
            
            for j in range(0,i):
                a -= L[i][j] * y[j][0]

            y[i][0] = a/L[i][i]
        
        return y

    @staticmethod
    def backwardSubstitution(U, b):

        if U.n != b.n:
            raise Exception("Uncompatabile matrices.")
        
        x = Matrix.initMatrix(b.n, 1) 
        n = b.n

        for i in range(n-1,-1,-1):
            a = b[i][0]
            
            for j in range(i+1,n):
                a -= U[i][j] * x[j][0]
                

            x[i][0] = a/U[i][i]
        
        return x

    def lu(matrix, precision = 1e-11): 
        """
        Implementation of a LU-decomposition algorith
        It decomposes the given matrix A to two matrices L and U.
        L is of lower triangular form while U is of upper triangular form.
        Their product L*U is the original matrix. An important class of matrices
        for which LU decomposition always works correctly is the
        class of symmetric positive-definite matrices
        """
        A = matrix.copy()
        n = A.n
        m = A.m
        
        for k in range(n):

            if Matrix.feq(precision, A[k][k], 0):
                raise Exception('(lu) Abort: Matrix is singular.')

            for i in range(k+1,n):
                A[i][k] /= A[k][k]
                for j in range(k+1,n):
                    A[i][j] -= A[i][k]*A[k][j]

        # can be better, see book655, 
        L = Matrix.eye(n,m)
        for i in range(0,n):
            for j in range(0,i):
                L[i][j] = A[i][j]
                
        U = Matrix. eye(n,m)
        for i in range(0,n):
            for j in range(i,n):
                U[i][j] = A[i][j]

        return L,U # or return A and create functions upperTriangular lowerTriangular

    def lup(matrix, precision = 1e-11): #649
        """
        Implementation of a LUP-decomposition algorith
        It decomposes the given matrix A to three matrices L, U and P.
        L is of lower triangular form while U is of upper triangular form and
        P is a permutation matrix.
        Their product L*U is the original matrix. An important class of matrices
        for which LU decomposition always works correctly is the
        class of symmetric positive-definite matrices
        """
        A = matrix.copy()
        n = A.n
        m = A.m
        pi = range(0,n)
        k_ = None

        for k in range(0,n):
            p = 0

            for i in range(k,n):
                if abs(A[i][k]) > p:
                    p = A[i][k]
                    k_ = i

            if Matrix.feq(precision, p, 0):
                raise Exception('(lup) Abort: Matrix is singular.')

            pi[k_], pi[k] = pi[k], pi[k_]

            for i in range(0,n):
                Matrix.indexswap(A, (k,i), (k_,i))

            for i in range(k+1,n):
                A[i][k] /= A[k][k]
                for j in range(k+1,n):
                    A[i][j] -= A[i][k]*A[k][j]
        
        L = Matrix.eye(n,m)
        for i in range(0,n):
            for j in range(0,i):
                L[i][j] = A[i][j]
                
        U = Matrix. eye(n,m)
        for i in range(0,n):
            for j in range(i,n):
                U[i][j] = A[i][j]
                
        P = Matrix.initMatrix(n,m)
        for i in range(0,n):
            P[i][pi[i]] = 1

        return L,U,P    

    def copy(self):
        "Makes a copy of the current matrix."
        return Matrix(deepcopy(self.matrix))

    def toform(self, formfunc=None):
        """
        Returns as a string, matrix in the given form. Form should be a function
        which knows how to print a matrix.
        """

        def octaveform(m):
            """Return matrix if format usuble to octave."""
            strmatrix = []
            for row in m.matrix:
                strmatrix.append('['+", ".join([str(el) for el in row])+'];')

            return '['+" ".join(strmatrix)+']'
        
        form = ""
        try:
            form = formfunc(self)
        except:
            print ('An error occured while transforming matrix representation'
                   'using octave form instead.')
            form = octaveform(self)
                            
        return form

    def toFile(self, filename):
        """Write matrix to file. File path is determined by a given method argument."""
        f = open(filename, 'w')
        f.write(repr(self))

    @staticmethod
    def lupSolve(A, b, precision=1e-12):        
        """
        Solve the system of linear equotations using the LUP decomposition.
        """

        L, U, P = Matrix.lup(A, precision)
        print 'L'
        print L
        print 'U'
        print U
        print 'P'
        print P
        y = Matrix.forwardSubstitution(L, P*b)
        print 'y'
        print y
        x = Matrix.backwardSubstitution(U, y) 
        print 'x'
        print x

        return x

    @staticmethod
    def luSolve(A, b, precision=1e-12):        
        """
        Solve the system of linear equotations using the LU decomposition.
        """

        L, U = A.lu()
        print 'L'
        print L
        print 'U'
        print U
        y = Matrix.forwardSubstitution(L, b)
        print 'y'
        print y
        x = Matrix.backwardSubstitution(U, y) 
        print 'x'
        print x

        return x

    # Some handy functions.
    @staticmethod
    def feq(precision = 1e-7,*nums):
        """Compare floating point numbers."""
        x = nums[0]
        
        for y in nums[1:]:
            if abs(x-y) >= precision:
                return False
                
        return True
                 
    @staticmethod
    def indexswap(m, ij, kl):
        """
        For a given matrix m, swap elements indexed by i,j and k,l.
        """
        i,j = ij; k,l = kl
        tmp = m[i][j]
        m[i][j] = m[k][l]
        m[k][l] = tmp


# END OF CLASS 



# A = Matrix([[1,2,0],[3,4,4],[5,6,3]])
# b = ~Matrix([[3,7,8]])

# Numerical stability : amplification of round-off errors leads to unstable behaviour.
# Singular matrix : no inverse, determinant = 0
# Linear dependence : linear combination of non-trivial vector is zero.
# Rank : largest set of lineary independent rows|columns
#        smallest number r for A, such that there exist B n x r and C r x m for which A=BC
# Full rank : Matrix n x n has full rank if its rank is n;
# Positive - definite matrices : n x n matrix for which ~xAx > 0 for all x of size n
# full column - rank (n x m, r = m) => positive -definite

# The advantage of computing an LUP decomposition for the matrix A is that linear systems can
# be solved more readily when they are triangular, as is the case for both matrices L and U .
# Having found an LUP decomposition for A, we can solve the equation (28.17) Ax = b by
# solving only triangular linear systems.

# The process by which we perform LU decomposition is called Gaussian elimination. We
# start by subtracting multiples of the first equation from the other equations so that the first
# variable is removed from those equations. Then, we subtract multiples of the second equation
# from the third and subsequent equations so that now the first and second variables are
# removed from them. We continue this process until the system that is left has an upper-
# triangular form—in fact, it is the matrix U. The matrix L is made up of the row multipliers
# that cause variables to be eliminated. (28-22)

# An important class of matrices for which LU decomposition always works
# correctly is the class of symmetric positive-definite matrices. Such
# matrices require no pivoting, and thus therecursive strategy outlined
# above can be employed without fear of dividing by 0.

# Ax = b
# LUx = Pb
# Ly = Pb # use forward to get y.. y=fs(L,P*b)
# Ux = y  # use backward to get x.. x = bs(U,y)
# 651 ! cormen
