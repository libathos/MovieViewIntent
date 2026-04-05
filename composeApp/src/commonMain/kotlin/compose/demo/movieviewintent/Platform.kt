package compose.demo.movieviewintent

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform