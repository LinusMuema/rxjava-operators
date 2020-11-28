object Data {
    data class User(val name: String, var age: Int, val location: String, var salary: Int)
    val users = listOf(
        User("Michael", 20, "Office", 500),
        User("Pam", 25, "Reception", 300),
        User("Jim", 22, "Sales", 250),
        User("Darell", 26, "Warehouse",350),
        User("Dwight", 31, "Sales", 225),
        User("Angela", 27, "Accounting",200),
        User("Oscar", 28, "Accounting", 350),
        User("Roy", 30, "Warehouse", 150)
    )
}