def g(str1) -> str:
    i = 0
    new_str = ""
    while i < len(str1)-1:
        new_str = new_str + str1[i+1]
        i = i +1

    return new_str


def f(str2) -> str:
    if len(str2) == 0:
        return ""

    elif len(str2) == 1:
        return str2

    else:
        return f(g(str2)) + str2[0]


def h(n,str3) -> str:
    while n!=1:
        if n%2 == 0: n = n/2
        else: n = 3*n +1
        str3 = f(str3)

    return str3


def pow(x,y) -> int:
    if y == 0: return 1
    else: return x * pow(x,y-1)



if __name__ == "__main__":
    print(h(1, "fruits"))
    print(h(2, "fruits"))
    print(h(5, "fruits"))
    print(h(pow(2, 1000000000000000), "fruits"))
    print(h(pow(2, 9831050005000007), "fruits"))