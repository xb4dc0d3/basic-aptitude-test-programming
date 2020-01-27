def binary_search(array, value, low, high):
    if high < low:
        return -1
    else:
        mid = (low + high)/2;
        if array[mid] > value:
            return binary_search(array, value, low, mid-1)
        elif array[mid] < value:
            return binary_search(array, value, mid+1, high)
        else:
            return mid
array = []
for i in xrange(10000):
    array.append(input())
for i in xrange(10000):
    value = input()
    answer = binary_search(array, value, 0, 9999)
    print("%d" % answer)