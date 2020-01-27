
T = int(input())


def countDivisibleNumber(A,B,K):
    if (A%K == 0) : return ((B/K) - (A/K)) +1
    else: return ((B/K) - (A/K))

for i in range(T):
    A = int(input())
    B = int(input())
    K = int(input())

    result = int(countDivisibleNumber(A,B,K))
    print("Case {}: {}".format(i+1,result))