import Data.User
import io.reactivex.rxjava3.core.Observable

fun main(){
    // paste the code here 🤓
}

fun creators(){
    /** [JUST] **/
    Observable.just(Data.users).subscribe { println(it) }

    /** [FROM] **/
    Observable.fromIterable(Data.users).subscribe { println(it) }

    /** [REPEAT] **/
    Observable.just("I am emitted").repeat(3).subscribe { println(it) }

    /** [RANGE] **/
    Observable.range(0, 3).subscribe { println(it) }

    /** [CREATE] **/
    Observable.create<User> { emitter -> Data.users.forEach { emitter.onNext(it) } }.subscribe { println(it) }
}

fun transformers(){

    /** [MAP] **/
    Observable.fromIterable(Data.users).map {
        it.age = it.age * 2
        it
    }.subscribe { println(it)}

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

    /** [GROUPBY] **/
    Observable.fromIterable(Data.users)
        .groupBy { it.location }
        .subscribe { if (it.getKey() == "Sales") it.subscribe { println(it) }}

    /** [BUFFER] **/
    Observable.fromIterable(Data.users).buffer(3).subscribe { println(it)}
}

fun filters(){
    /** [FILTER] **/
    Observable.fromIterable(Data.users).filter {it.age > 25}.subscribe { println(it)}

    /** [TAKE] **/
    Observable.fromIterable(Data.users).take(3).subscribe { println(it)}

    /** [TAKELAST] **/
    Observable.fromIterable(Data.users).takeLast(3).subscribe { println(it)}

    /** [SKIP] **/
    Observable.fromIterable(Data.users).skip(3).subscribe { println(it)}

    /** [SKIPLAST] **/
    Observable.fromIterable(Data.users).skipLast(3).subscribe { println(it)}

    /** [ELEMENTAT] **/
    Observable.fromIterable(Data.users).elementAt(3).subscribe { println(it)}

    /** [DISTINCT] **/
    Observable.just('a', 'a', 'b', 'b', 'c', 'c').distinct().subscribe { println(it) }

}