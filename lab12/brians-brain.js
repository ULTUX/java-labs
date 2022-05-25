/**
 * 0 - off, 1 - on, 2 - dying
 * @param {[[]]} matrix
 * @returns {*}
 */
var nextGeneration = function (matrix) {
    var newMatrix = [];

    for (var i = 0; i < matrix.length; i++) {
        newMatrix.push([]);
        for (var j = 0; j < matrix[i].length; j++) {
            newMatrix[i].push(matrix[i][j]);
        }
    }

    for (var i = 0; i < matrix.length; i++){
        for (var j = 0; j < matrix[i].length; j++){
            var neighbours = 0;

            if (matrix[i][j] === 0)
                for (var x = -1; x < 2; x++){
                    for (var y = -1; y < 2; y++){
                        if (x === 0 && y === 0) continue;
                        if ((x + i >= 0 && x + i < matrix.length)
                            && (y + j >= 0 && y + j < matrix[i].length)){
                            if (matrix[i+x][j+y] === 1) neighbours++;
                        }
                    }
                }
            if (matrix[i][j] === 1) newMatrix[i][j] = 2;
            if (matrix[i][j] === 2) newMatrix[i][j] = 0;
            if (neighbours === 2) newMatrix[i][j] = 1;


        }
    }
    return newMatrix;
};