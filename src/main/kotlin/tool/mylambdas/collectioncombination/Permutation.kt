package tool.mylambdas.collectioncombination


// return all combinations of split in (split, splitSize)
//   (   split   )
//   ( splitSize )
//
fun <T> List<T>.getCombinationList(splitSize: Int): List<List<T>> {
    val data = this.take(splitSize).toMutableList()
    val arr = this.toMutableList()
    val allCombinationsList = mutableListOf<List<T>>()
    combinationUtil(arr, data, 0, this.size - 1, 0, splitSize, allCombinationsList)
    return allCombinationsList
}

/* This code is contributed by Devesh Agrawal (and altered by myself) */
private fun <T> combinationUtil(arr: MutableList<T>, data: MutableList<T>, start: Int, end: Int, index: Int, r: Int, allCombinationsList: MutableList<List<T>>) {
    if (index == r) {
        allCombinationsList.add(data.toList())
        return
    }

    var i = start
    while (i <= end && end - i + 1 >= r - index) {
        data[index] = arr[i]
        combinationUtil(arr, data, i + 1, end, index + 1, r, allCombinationsList)
        i++
    }
}

/**
 * returns all permutations of the given list.
 * each 'list' in the returned list of 'list's, contains one of the permutations.
 * be aware of the combinatorial explosion!
 *
 */

fun <T> List<T>.makeAllPermutations(): List<List<T>> {
    return makeAllPermutations(this.size, this.toMutableList())
}

private fun <T> swap(elements: MutableList<T>, a: Int, b: Int) {
    val tmp = elements[a]
    elements[a] = elements[b]
    elements[b] = tmp
}

private fun <T> makeAllPermutations(n: Int, elements: MutableList<T>): List<List<T>> {
    if (n == 1) {
        return listOf(elements.toList())
    } else {
        val localList = mutableListOf<List<T>>()
        for (i in 0.. n - 2) {
            localList.addAll(makeAllPermutations(n - 1, elements))
            if (n % 2 == 0) {
                swap(elements, i, n - 1)
            } else {
                swap(elements, 0, n - 1)
            }
        }
        localList.addAll(makeAllPermutations(n - 1, elements))
        return localList
    }
}

/**
 * return all combinations in a list of sets.
 * example we have a list of (a, b, c)
 * then this functions returns
 *   (), (a), (b), (c), (a,b), (a,c), (b,c), (a,b,c)
 *
 * not necessarily in that order
 */
fun <T> List<T>.toPowerList(): List<List<T>> =
    fold(listOf(emptyList())) { acc, e ->
        acc + acc.map { it + e }
    }
