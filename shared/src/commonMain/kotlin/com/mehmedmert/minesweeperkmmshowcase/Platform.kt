package com.mehmedmert.minesweeperkmmshowcase

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform