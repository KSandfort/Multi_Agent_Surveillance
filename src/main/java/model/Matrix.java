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

    public Matrix add(double value)
    {
        Matrix output = new Matrix(rows,columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                output.values[i][j] = values[i][j] + value;
            }
        }
        return output;
    }

    public Matrix add(Matrix m)
    {
        if(rows != m.rows || columns != m.columns)
        {
            throw new IllegalArgumentException("Shape of matrices does not match ");
        }
        Matrix output = new Matrix(rows,columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                output.values[i][j] = values[i][j] + m.values[i][j];
            }
        }
        return output;
    }

    public Matrix subtract(double value)
    {
        Matrix output = new Matrix(rows,columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                output.values[i][j] = values[i][j] - value;
            }
        }
        return output;
    }

    public Matrix subtract(Matrix m)
    {
        if(rows != m.rows || columns != m.columns)
        {
            throw new IllegalArgumentException("Shape of matrices does not match ");
        }

        Matrix output = new Matrix(rows,columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                output.values[i][j] = values[i][j] - m.values[i][j];
            }
        }
        return output;
    }

    //matrix multiplication
    public Matrix mMultiply(Matrix m)
    {
        if(m.rows != columns)
        {
            throw new IllegalArgumentException("Shapes of matrices are incorrect");
        }

        Matrix output = new Matrix(rows,m.columns);

        for(int i=0;i<output.rows;i++)
        {
            for(int j=0;j<output.columns;j++)
            {
                double sum=0;
                for(int k=0;k<columns;k++)
                {
                    sum+=values[i][k]*m.values[k][j];
                }
                output.values[i][j]=sum;
            }
        }
        return output;
    }


    //element wise multiplication
    public Matrix eMultiply(Matrix m)
    {
        if(rows != m.rows || columns != m.columns)
        {
            throw new IllegalArgumentException("Shape of matrices does not match ");
        }

        Matrix output = new Matrix(rows,columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                output.values[i][j] = values[i][j] * m.values[i][j];
            }
        }
        return output;
    }

    //multiply by scalar
    public Matrix sMultiply(double a)
    {
        Matrix output = new Matrix(rows,columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                output.values[i][j] = values[i][j] * a;
            }
        }
        return output;
    }

    public Matrix transpose()
    {
        Matrix output = new Matrix(columns, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                output.values[j][i] = values[i][j];
            }
        }
        return output;
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

