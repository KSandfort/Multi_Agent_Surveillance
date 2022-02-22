package model;

public class Matrix {
    public double[][] values;
    public int rows;
    public int columns;
    public Matrix(int x,int y)
    {
        rows = x;
        columns = y;
        values = new double[x][y];
    }

    public Matrix(double[][] values)
    {
        this.values = values;
        rows = values.length;
        columns = values[0].length;
    }

    public void add(double value)
    {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                values[i][j] += value;
            }
        }
    }

    public void add(Matrix m)
    {
        if(rows != m.rows || columns != m.columns)
        {
            throw new IllegalArgumentException("Shape of matrices does not match ");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                values[i][j] += m.values[i][j];
            }
        }
    }

    public void subtract(double value)
    {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                values[i][j] -= value;
            }
        }
    }

    public void subtract(Matrix m)
    {
        if(rows != m.rows || columns != m.columns)
        {
            throw new IllegalArgumentException("Shape of matrices does not match ");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                values[i][j] -= m.values[i][j];
            }
        }
    }

    //matrix multiplication
    public Matrix mMultiply(Matrix m)
    {
        if(m.rows != columns)
        {
            throw new IllegalArgumentException("Shapes of matrices are incorrect");
        }

        Matrix result = new Matrix(rows,m.columns);

        for(int i=0;i<result.rows;i++)
        {
            for(int j=0;j<result.columns;j++)
            {
                double sum=0;
                for(int k=0;k<columns;k++)
                {
                    sum+=values[i][k]*m.values[k][j];
                }
                result.values[i][j]=sum;
            }
        }
        return result;
    }


    //element wise multiplication
    public void eMultiply(Matrix m)
    {
        if(rows != m.rows || columns != m.columns)
        {
            throw new IllegalArgumentException("Shape of matrices does not match ");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                values[i][j] *= m.values[i][j];
            }
        }
    }

    //multiply by scalar
    public void sMultiply(double a)
    {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                values[i][j] *= a;
            }
        }
    }

    public Matrix sigmoid()
    {
        Matrix output = new Matrix(rows,columns);

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++) {
                output.values[i][j] = 1/(1 + Math.exp(-values[i][j]));
            }
        }
        return output;
    }

    public Matrix dSigmoid()
    {
        Matrix output = sigmoid();

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++) {
                output.values[i][j] = output.values[i][j] * (1 - output.values[i][j]);
            }
        }

        return output;
    }
}

