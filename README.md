### Deep dive into RxJava Operators
RxJava has very many operators. And going through the documentation might be a bit tedious for a beginner. This is because it uses a theoretical approach rather than a practical one. In the previous [article](https://www.section.io/engineering-education/android-rxjava/), we went through the basics of RxJava and how to use it in Android. The article addressed the various types of observers and their properties. However, we did not look at how to create the RxJava observables and how they manipulate data. This article goes through exactly that.

RxJava makes use of operators to create the observables as well as manipulate the data the observables receive. There are very many observables that have various functions. These functions can also have similar functions allowing us to group them. We will only look at the few operators in each group. You can get the code used in this article on [GitHub](https://github.com/LinusMuema/rxjava-operators).

### Prerequisites
In order to follow through this article, you need:
* [Intellij IDE](https://www.jetbrains.com/idea/) installed
* Basic understanding of the Kotlin programming language
* Basic understanding of RxJava observables. You can read [this article](https://www.section.io/engineering-education/android-rxjava/) to get you up to speed.

Go ahead and clone the repository using Intellij and wait for the gradle build to finish. Once done, navigate to the `src/main/kotlin` directory. We will be working from this folder. In the `main.kt` file, we have three functions that contain RxJava operators based on their functionalities. We will be removing the comments and running the application to get the results. There is also the `Data.kt` file, which contains sample data we will use.

### Grouping
We can group the operators according to their functions. Some of these functions include:
* _**creation**_: these operators are used to create observables.
* _**transformation**_: we use these operators to change the data that the observers receive, i.e., transform the data
* _**filtering**_: these are used to determine what data is emitted using a specified criteria

#### `creation`
These operators are used to create observables. We can create observables from a wide variety of items like lists and ranges. The operators include:

- **just**: this operator is used to create an observable from the object passed in as an argument. It converts the object to an observable. Copy the following code in the `main` function and run the application.

```kotlin
fun main(){
    /** [JUST] **/
    Observable.just(Data.users).subscribe { println(it) }
}
```

Output:
```bash
[User(name=Michael, age=20, location=Office, salary=500), User(name=Pam, age=25, location=Reception, salary=300), User(name=Jim, age=22, location=Sales, salary=250), User(name=Darell, age=26, location=Warehouse, salary=350), User(name=Dwight, age=31, location=Sales, salary=225), User(name=Angela, age=27, location=Accounting, salary=200), User(name=Oscar, age=28, location=Accounting, salary=350), User(name=Roy, age=30, location=Warehouse, salary=150)]
```

- **from**: this also generates an observable from the item passed in as an argument. The difference between `just` and `from` is in their nature of emission. If we pass in a list or an iterable, `from` emits each item in the list/iterable separately unlike `just` which emits the entire list. `from` has been replaced by more specific methods like `fromIterable`, `fromArray`, `fromStream`, etc. Replace the code in the main function with the one below and run.

```kotlin
fun main(){
    /** [FROM] **/
    Observable.fromIterable(Data.users).subscribe { println(it) }
}
```

Output:
```bash
User(name=Michael, age=20, location=Office, salary=500)
User(name=Pam, age=25, location=Reception, salary=300)
User(name=Jim, age=22, location=Sales, salary=250)
User(name=Darell, age=26, location=Warehouse, salary=350)
User(name=Dwight, age=31, location=Sales, salary=225)
User(name=Angela, age=27, location=Accounting, salary=200)
User(name=Oscar, age=28, location=Accounting, salary=350)
User(name=Roy, age=30, location=Warehouse, salary=150)
```
- **repeat**: as the name says, this creates an observable that emits data a specified number of times. If we do not pass in any number, it will lead to an infinite loop.

```kotlin
fun main(){
    /** [REPEAT] **/
    Observable.just("I am emitted").repeat(3).subscribe { println(it) }
}
```

Output:
```bash
I am emitted
I am emitted
I am emitted
```
- **range**: this creates an observable from a range of values. You can read more about ranges in [this article](https://www.section.io/engineering-education/kotlin-ranges/).

```kotlin
fun main(){
    /** [RANGE] **/
    Observable.range(0, 3).subscribe { println(it) }
}
```

Output :
```bash
0
1
2

```
- **create**:  this operator creates an observable from scratch. It gives the developer the freedom to choose when to call the `onNext`, `onComplete`, and `onError` methods and what data/exception to pass in them. We loop through the list in our code and call the onNext method passing in each item in the loop.

```kotlin   
fun main(){
    /** [CREATE] **/
    Observable.create<User> { emitter ->
        Data.users.forEach { emitter.onNext(it) }
    }.subscribe { println(it) }
}
```
Output:
```bash
User(name=Michael, age=20, location=Office, salary=500)
User(name=Pam, age=25, location=Reception, salary=300)
User(name=Jim, age=22, location=Sales, salary=250)
User(name=Darell, age=26, location=Warehouse, salary=350)
User(name=Dwight, age=31, location=Sales, salary=225)
User(name=Angela, age=27, location=Accounting, salary=200)
User(name=Oscar, age=28, location=Accounting, salary=350)
User(name=Roy, age=30, location=Warehouse, salary=150)
```

#### `transformation`
Sometimes when observables receive data, they may need to change or manipulate it to the desired output. We have operators to do just that. Some of the transformation operators are:

- **map**: this method applies a function to each of the items emitted by the observable. In our code, we use lambda functions to change the `User` object emitted. We double the age property and return the item, which is then emitted.

```kotlin
fun main(){
    /** [MAP] **/
    Observable.fromIterable(Data.users).map {
        it.age = it.age * 2
        it
    }.subscribe { println(it)}
}
```

Output:

```bash
User(name=Michael, age=40, location=Office, salary=500)
User(name=Pam, age=50, location=Reception, salary=300)
User(name=Jim, age=44, location=Sales, salary=250)
User(name=Darell, age=52, location=Warehouse, salary=350)
User(name=Dwight, age=62, location=Sales, salary=225)
User(name=Angela, age=54, location=Accounting, salary=200)
User(name=Oscar, age=56, location=Accounting, salary=350)
User(name=Roy, age=60, location=Warehouse, salary=150)
```
- **flatMap**: this works similar to `map`, but instead of returning the item itself, it returns an observable that can also emit data. A good use case is when combining observables that depend on each other. In our code, we have a function `upgrade` which changes the salary property based on age. It then returns an observable containing the modified object.

```kotlin
fun main(){
    /** [FLATMAP] && [CONCATMAP]**/
    fun upgrade(user: User): Observable<User> {
        when {
            user.age > 25 -> user.salary = user.salary * 2
            user.age > 30 -> user.salary = user.salary * 3
            else -> user.salary = user.salary * 4
        }
        return Observable.just(user)
    }
    Observable.fromIterable(Data.users).flatMap{ upgrade(it) }.subscribe { println(it)}
}
```

Output:
```bash
User(name=Michael, age=20, location=Office, salary=2000)
User(name=Pam, age=25, location=Reception, salary=1200)
User(name=Jim, age=22, location=Sales, salary=1000)
User(name=Darell, age=26, location=Warehouse, salary=700)
User(name=Dwight, age=31, location=Sales, salary=450)
User(name=Angela, age=27, location=Accounting, salary=400)
User(name=Oscar, age=28, location=Accounting, salary=700)
User(name=Roy, age=30, location=Warehouse, salary=300)
```
- **concatMap**: this operator does the same work as `flatMap` but with one difference. It keeps the order in which it receives the data. `flatMap` can lead to data changing in terms of position and time emitted. Since our data is static, the difference is not visible. We get the same result after replacing `flatMap` with `concatMap`.

- **groupBy**: this operator collects data according to the property used. In simpler words, it groups the items emitted based on the property defined. In our code, we use the location property to group the data. We can then access the data based on the group keys. We get data whose location property is `Sales`. You can go ahead and test the other location properties too.

```kotlin
fun main(){
    /** [GROUPBY] **/
    Observable.fromIterable(Data.users)
        .groupBy { it.location }
        .subscribe { if (it.getKey() == "Sales") it.subscribe { println(it) }}
}
```

Output:
```bash
User(name=Jim, age=22, location=Sales, salary=250)
User(name=Dwight, age=31, location=Sales, salary=225)
```
- **buffer**: this emits a specific number of items at a time. You pass in the number as an argument. In our case, we specify three items to be emitted at the same time.

```kotlin
fun main(){
    /** [BUFFER] **/
    Observable.fromIterable(Data.users).buffer(3).subscribe { println(it)}
}
```

Output
```bash
[User(name=Michael, age=20, location=Office, salary=500), User(name=Pam, age=25, location=Reception, salary=300), User(name=Jim, age=22, location=Sales, salary=250)]
[User(name=Darell, age=26, location=Warehouse, salary=350), User(name=Dwight, age=31, location=Sales, salary=225), User(name=Angela, age=27, location=Accounting, salary=200)]
[User(name=Oscar, age=28, location=Accounting, salary=350), User(name=Roy, age=30, location=Warehouse, salary=150)]
```

#### `filtering`
As the use says, they emit data that meets the specified criteria. The operators used are:

- **filter**: the observables only emit the values that meet the specified predicate. We specify that only records whose age property is above 25 should be emitted.

```kotlin
fun main(){
    /** [FILTER] **/
    Observable.fromIterable(Data.users).filter {it.age > 25}.subscribe { println(it)}
}
```

Output:
```bash
User(name=Darell, age=26, location=Warehouse, salary=350)
User(name=Dwight, age=31, location=Sales, salary=225)
User(name=Angela, age=27, location=Accounting, salary=200)
User(name=Oscar, age=28, location=Accounting, salary=350)
User(name=Roy, age=30, location=Warehouse, salary=150)
```
- **take**: the observable emits the specified number of items starting from the first. We pass in three as an argument, and only the first three items from the list are emitted. The opposite of this operator is the `takeLast`. It emits the last number of specified items.

```kotlin
fun main(){
    /** [TAKE] **/
    Observable.fromIterable(Data.users).take(3).subscribe { println(it)}
}
```

Output:
```bash
User(name=Michael, age=20, location=Office, salary=500)
User(name=Pam, age=25, location=Reception, salary=300)
User(name=Jim, age=22, location=Sales, salary=250)
```
- **skip**: this operator is used to prevent the emission of the items. As the name states, the observable skips the specified number of items. In our code, we pass in three, so the first three items are not emitted. The opposite of this operator is `skipLast`, which skips the last number of items specified.

```kotlin
fun main(){
    /** [SKIP] **/
    Observable.fromIterable(Data.users).skip(3).subscribe { println(it)}
}
```

Output:
```bash
User(name=Darell, age=26, location=Warehouse, salary=350)
User(name=Dwight, age=31, location=Sales, salary=225)
User(name=Angela, age=27, location=Accounting, salary=200)
User(name=Oscar, age=28, location=Accounting, salary=350)
User(name=Roy, age=30, location=Warehouse, salary=150)
```
- **elementAt**: this only emits the item in the specified position. If we pass 3, only the third item on the list is emitted.

```kotlin
fun main(){
    /** [ELEMENTAT] **/
    Observable.fromIterable(Data.users).elementAt(3).subscribe { println(it)}
}
```

Output:
```bash
User(name=Darell, age=26, location=Warehouse, salary=350)
```

- **distinct**: this is used to emit the distinct values in the list, i.e., the cast items do not repeat.

```kotlin
fun main(){
    /** [DISTINCT] **/
    Observable.just('a', 'a', 'b', 'b', 'c', 'c').distinct().subscribe { println(it) }
}
```

Output:
```bash
a
b
c
```

### Conclusion
And with that, you have a basic understanding of how RxJava operators work. It's is a good practice to test each of the operators. They are used in most RxJava observables to work with data and manipulate it. They give the developer the freedom to modify the behavior and properties of the observables themselves. Go ahead and try out the other operators from the [official documentation](http://reactivex.io/documentation/operators.html). If any error comes up or an issue, feel free to raise an issue. 🤓
