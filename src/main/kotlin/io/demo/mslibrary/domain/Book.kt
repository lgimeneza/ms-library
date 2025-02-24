package io.demo.mslibrary.domain

data class Book(
    val id: BookId,
    val title: Title,
    val author: Author,
    val category: Category,
    val publishedYear: PublishedYear,
    val isbn: Isbn?
) {
    companion object {
        fun create(
            title: Title,
            author: Author,
            category: Category,
            publishedYear: PublishedYear,
            isbn: Isbn? = null
        ): Book {
            return Book(BookId.generate(), title, author, category, publishedYear, isbn)
        }
    }
}
