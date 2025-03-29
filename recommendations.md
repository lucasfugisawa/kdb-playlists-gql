# Recommendations for Decoupling Entities from Exposed ORM

## Current Issues

The current implementation tightly couples the domain entities with the Exposed ORM framework:

1. **Direct Inheritance**: Entities like `User`, `Song`, and `Playlist` directly inherit from Exposed's `UUIDEntity` class.
2. **Table Definitions**: Table definitions (`Users`, `Songs`, etc.) are defined in the same files as the entities.
3. **Exposed-specific Annotations**: The code uses Exposed-specific annotations and types throughout the domain model.
4. **Repository Implementation**: Repositories directly use Exposed's DSL for querying.

This tight coupling creates several problems:
- **Reduced Testability**: It's difficult to test domain logic without involving the database.
- **Limited Flexibility**: Changing the ORM or database access strategy would require rewriting a significant portion of the codebase.
- **Leaky Abstraction**: Database concerns leak into the domain model.
- **Increased Learning Curve**: New developers need to understand Exposed to work with the domain model.

## Recommended Approach: Domain Model with Data Mapper Pattern

I recommend implementing a clear separation between domain models and persistence models using the Data Mapper pattern:

### Architecture Overview

1. **Domain Models**: Pure Kotlin classes that represent business entities without any ORM dependencies.
2. **Data Access Objects (DAOs)**: Exposed entities that handle persistence concerns.
3. **Mappers**: Classes responsible for converting between domain models and DAOs.
4. **Repositories**: Interfaces that define data access methods, with implementations that use Exposed.

### Benefits

- **Improved Testability**: Domain models can be tested without database dependencies.
- **Flexibility**: The ORM can be changed without affecting the domain model.
- **Clear Separation of Concerns**: Domain logic is separated from persistence logic.
- **Simplified Domain Model**: Domain models become simpler and more focused on business rules.

## Implementation Example

Here's how we could implement this approach for the `Song` entity:

### 1. Domain Model

```kotlin
// src/main/kotlin/com/fugisawa/playlistsgql/domain/models/Song.kt
// Domain model without ORM dependencies

data class Song(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val artist: String,
    val duration: Int,
    val genre: Genre
)
```

### 2. Data Access Object (DAO)

```kotlin
// src/main/kotlin/com/fugisawa/playlistsgql/data/dao/SongDao.kt
// DAO with Exposed dependencies

// Table definition
object SongTable : UUIDTable("songs") {
    val title: Column<String> = varchar("title", 255)
    val artist: Column<String> = varchar("artist", 255)
    val duration: Column<Int> = integer("duration")
    val genre: Column<String> = varchar("genre", 50)
}

// DAO definition
class SongDao(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SongDao>(SongTable)

    var title by SongTable.title
    var artist by SongTable.artist
    var duration by SongTable.duration
    var genre by SongTable.genre

    // Helper property to convert string to enum
    var genreEnum: Genre
        get() = Genre.valueOf(genre)
        set(value) { genre = value.name }
}
```

### 3. Mapper

```kotlin
// src/main/kotlin/com/fugisawa/playlistsgql/data/mappers/SongMapper.kt
// Mapper between domain model and DAO

object SongMapper {
    fun toEntity(dao: SongDao): Song {
        return Song(
            id = dao.id.value,
            title = dao.title,
            artist = dao.artist,
            duration = dao.duration,
            genre = dao.genreEnum
        )
    }

    fun toDao(entity: Song): SongDao {
        return SongDao.new(entity.id) {
            title = entity.title
            artist = entity.artist
            duration = entity.duration
            genreEnum = entity.genre
        }
    }

    fun toEntities(daos: List<SongDao>): List<Song> {
        return daos.map { toEntity(it) }
    }
}
```

### 4. Repository Interface

```kotlin
// src/main/kotlin/com/fugisawa/playlistsgql/domain/repositories/SongRepository.kt
// Repository interface for Song entity

interface SongRepository {
    suspend fun getById(id: UUID): Song?
    suspend fun getAll(): List<Song>
    suspend fun findByTitle(title: String): List<Song>
    suspend fun findByArtist(artist: String): List<Song>
    suspend fun findByGenre(genre: Genre): List<Song>
    suspend fun create(song: Song): Song
    suspend fun update(song: Song): Song
    suspend fun delete(id: UUID): Boolean
}
```

### 5. Repository Implementation

```kotlin
// src/main/kotlin/com/fugisawa/playlistsgql/data/repositories/SongRepositoryImpl.kt
// Repository implementation using Exposed

class SongRepositoryImpl : SongRepository {
    override suspend fun getById(id: UUID): Song? = dbQuery {
        SongDao.findById(id)?.let { SongMapper.toEntity(it) }
    }

    override suspend fun getAll(): List<Song> = dbQuery {
        SongMapper.toEntities(SongDao.all().toList())
    }

    override suspend fun findByTitle(title: String): List<Song> = dbQuery {
        SongMapper.toEntities(SongDao.find { SongTable.title eq title }.toList())
    }

    override suspend fun findByArtist(artist: String): List<Song> = dbQuery {
        SongMapper.toEntities(SongDao.find { SongTable.artist eq artist }.toList())
    }

    override suspend fun findByGenre(genre: Genre): List<Song> = dbQuery {
        SongMapper.toEntities(SongDao.find { SongTable.genre eq genre.name }.toList())
    }

    override suspend fun create(song: Song): Song = dbQuery {
        val dao = SongMapper.toDao(song)
        SongMapper.toEntity(dao)
    }

    override suspend fun update(song: Song): Song = dbQuery {
        val dao = SongDao.findById(song.id) ?: throw IllegalArgumentException("Song not found")
        dao.title = song.title
        dao.artist = song.artist
        dao.duration = song.duration
        dao.genreEnum = song.genre
        SongMapper.toEntity(dao)
    }

    override suspend fun delete(id: UUID): Boolean = dbQuery {
        val dao = SongDao.findById(id) ?: return@dbQuery false
        dao.delete()
        true
    }
}
```

### 6. Repository Factory

```kotlin
// src/main/kotlin/com/fugisawa/playlistsgql/data/repositories/RepositoryFactory.kt
// Factory for creating repository instances

object RepositoryFactory {
    // Lazy-initialized repository instances
    val songRepository: SongRepository by lazy { SongRepositoryImpl() }
    // Other repositories...
}

fun Application.configureRepositories() {
    // Initialize repositories by accessing them
    RepositoryFactory.songRepository
    // Other repositories...

    log.info("Repositories configured")
}
```

## Migration Strategy

To migrate from the current architecture to the recommended one:

1. **Start with One Entity**: Begin with a single entity (like Song) to validate the approach.
2. **Create New Package Structure**: Set up the new package structure without modifying existing code.
3. **Implement Domain Models**: Create pure domain models for each entity.
4. **Rename Existing Entities**: Rename existing entities to DAOs (e.g., `Song` to `SongDao`).
5. **Create Mappers**: Implement mappers to convert between domain models and DAOs.
6. **Update Repositories**: Modify repositories to use the new domain models and mappers.
7. **Update Service Layer**: Update any service classes to use the domain models instead of DAOs.
8. **Update Controllers/Resolvers**: Update controllers or GraphQL resolvers to use domain models.
9. **Remove Direct Dependencies**: Gradually remove direct dependencies on Exposed from non-data layers.

## Potential Challenges and Solutions

1. **Increased Boilerplate Code**: 
   - Solution: Consider using code generation tools or Kotlin's extension functions to reduce boilerplate.

2. **Performance Overhead**: 
   - Solution: Use efficient mapping techniques and consider caching for frequently accessed data.

3. **Learning Curve**: 
   - Solution: Document the new architecture thoroughly and provide examples for common use cases.

4. **Backward Compatibility**: 
   - Solution: Maintain backward compatibility during migration by keeping existing APIs unchanged.

5. **Complex Relationships**: 
   - Solution: Use lazy loading or explicit loading strategies for relationships between entities.

## Conclusion

Implementing the Data Mapper pattern will significantly improve the maintainability, testability, and flexibility of the codebase. While it requires some upfront investment in terms of development effort, the long-term benefits outweigh the costs, especially as the application grows in complexity.

The recommended approach aligns with industry best practices for enterprise applications and provides a solid foundation for future growth and changes in requirements or technology stack.
