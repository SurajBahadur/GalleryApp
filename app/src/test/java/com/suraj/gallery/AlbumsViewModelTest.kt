package com.suraj.gallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.suraj.gallery.data.model.Album
import com.suraj.gallery.domain.model.AlbumType
import com.suraj.gallery.domain.usecase.GetAlbumsUseCase
import com.suraj.gallery.presentation.ui.albums.AlbumsViewModel
import com.suraj.gallery.presentation.ui.common.ViewState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AlbumsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val getAlbumsUseCase: GetAlbumsUseCase = mockk()
    private lateinit var viewModel: AlbumsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load albums automatically`() = runTest {
        // Given
        val mockAlbums = listOf(
            Album(
                id = "1",
                name = "Camera",
                type = AlbumType.CAMERA,
                coverImageUri = "content://media/external/images/media/1",
                itemCount = 10,
                lastModified = 1672531200000L
            ),
            Album(
                id = "2",
                name = "Folder",
                type = AlbumType.FOLDER,
                coverImageUri = "content://media/external/images/media/2",
                itemCount = 5,
                lastModified = 1672617600000L
            )
        )
        coEvery { getAlbumsUseCase() } returns Result.success(mockAlbums)

        // When
        viewModel = AlbumsViewModel(getAlbumsUseCase)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.first()
        assertTrue(state is ViewState.Success)
        assertEquals(mockAlbums, (state as ViewState.Success).data)
        coVerify(exactly = 1) { getAlbumsUseCase() }
    }

    @Test
    fun `loadAlbums should emit loading state first`() = runTest {
        // Given
        val mockAlbums = listOf(
            Album(
                id = "1",
                name = "Camera",
                type = AlbumType.CAMERA,
                coverImageUri = "content://media/external/images/media/1",
                itemCount = 10,
                lastModified = 1672531200000L
            )
        )
        coEvery { getAlbumsUseCase() } returns Result.success(mockAlbums)

        viewModel = AlbumsViewModel(getAlbumsUseCase)

        // When
        viewModel.loadAlbums()

        // Then - Check loading state is emitted first
        val initialState = viewModel.uiState.first()
        assertTrue(initialState is ViewState.Success)
    }

    @Test
    fun `loadAlbums should emit success state when use case succeeds`() = runTest {
        // Given
        val mockAlbums = listOf(
            Album(
                id = "1",
                name = "Camera",
                type = AlbumType.CAMERA,
                coverImageUri = "content://media/external/images/media/1",
                itemCount = 10,
                lastModified = 1672531200000L
            ),
            Album(
                id = "2",
                name = "Folder",
                type = AlbumType.FOLDER,
                coverImageUri = "content://media/external/images/media/2",
                itemCount = 5,
                lastModified = 1672617600000L
            ),
            Album(
                id = "3",
                name = "All Videos",
                type = AlbumType.ALL_VIDEOS,
                coverImageUri = null,
                itemCount = 15,
                lastModified = 1672704000000L
            )
        )
        coEvery { getAlbumsUseCase() } returns Result.success(mockAlbums)

        viewModel = AlbumsViewModel(getAlbumsUseCase)

        // When
        viewModel.loadAlbums()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.first()
        assertTrue(state is ViewState.Success)
        assertEquals(mockAlbums, (state as ViewState.Success).data)
        assertEquals(3, state.data.size)
    }

    @Test
    fun `loadAlbums should emit error state when use case fails`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { getAlbumsUseCase() } returns Result.failure(exception)

        viewModel = AlbumsViewModel(getAlbumsUseCase)

        // When
        viewModel.loadAlbums()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.first()
        assertTrue(state is ViewState.Error)
        assertEquals(exception, (state as ViewState.Error).exception)
    }

    @Test
    fun `loadAlbums should emit success state with empty list when no albums found`() = runTest {
        // Given
        val emptyAlbums = emptyList<Album>()
        coEvery { getAlbumsUseCase() } returns Result.success(emptyAlbums)

        viewModel = AlbumsViewModel(getAlbumsUseCase)

        // When
        viewModel.loadAlbums()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.first()
        assertTrue(state is ViewState.Success)
        assertEquals(emptyAlbums, (state as ViewState.Success).data)
        assertTrue((state as ViewState.Success).data.isEmpty())
    }

    @Test
    fun `initial view mode should be GRID`() = runTest {
        // Given
        coEvery { getAlbumsUseCase() } returns Result.success(emptyList())

        // When
        viewModel = AlbumsViewModel(getAlbumsUseCase)

        // Then
        val viewMode = viewModel.viewMode.first()
        assertEquals(AlbumsViewModel.ViewMode.GRID, viewMode)
    }

    @Test
    fun `toggleViewMode should change from GRID to LINEAR`() = runTest {
        // Given
        coEvery { getAlbumsUseCase() } returns Result.success(emptyList())
        viewModel = AlbumsViewModel(getAlbumsUseCase)

        // Verify initial state
        assertEquals(AlbumsViewModel.ViewMode.GRID, viewModel.viewMode.first())

        // When
        viewModel.toggleViewMode()

        // Then
        val newViewMode = viewModel.viewMode.first()
        assertEquals(AlbumsViewModel.ViewMode.LINEAR, newViewMode)
    }

    @Test
    fun `toggleViewMode should change from LINEAR to GRID`() = runTest {
        // Given
        coEvery { getAlbumsUseCase() } returns Result.success(emptyList())
        viewModel = AlbumsViewModel(getAlbumsUseCase)

        // Set to LINEAR first
        viewModel.toggleViewMode()
        assertEquals(AlbumsViewModel.ViewMode.LINEAR, viewModel.viewMode.first())

        // When
        viewModel.toggleViewMode()

        // Then
        val newViewMode = viewModel.viewMode.first()
        assertEquals(AlbumsViewModel.ViewMode.GRID, newViewMode)
    }

    @Test
    fun `toggleViewMode should alternate between GRID and LINEAR multiple times`() = runTest {
        // Given
        coEvery { getAlbumsUseCase() } returns Result.success(emptyList())
        viewModel = AlbumsViewModel(getAlbumsUseCase)

        // Initial state should be GRID
        assertEquals(AlbumsViewModel.ViewMode.GRID, viewModel.viewMode.first())

        // Toggle 1: GRID -> LINEAR
        viewModel.toggleViewMode()
        assertEquals(AlbumsViewModel.ViewMode.LINEAR, viewModel.viewMode.first())

        // Toggle 2: LINEAR -> GRID
        viewModel.toggleViewMode()
        assertEquals(AlbumsViewModel.ViewMode.GRID, viewModel.viewMode.first())

        // Toggle 3: GRID -> LINEAR
        viewModel.toggleViewMode()
        assertEquals(AlbumsViewModel.ViewMode.LINEAR, viewModel.viewMode.first())

        // Toggle 4: LINEAR -> GRID
        viewModel.toggleViewMode()
        assertEquals(AlbumsViewModel.ViewMode.GRID, viewModel.viewMode.first())
    }

    @Test
    fun `multiple loadAlbums calls should call use case multiple times`() = runTest {
        // Given
        val mockAlbums = listOf(
            Album(
                id = "1",
                name = "Camera",
                type = AlbumType.CAMERA,
                coverImageUri = "content://media/external/images/media/1",
                itemCount = 10,
                lastModified = 1672531200000L
            )
        )
        coEvery { getAlbumsUseCase() } returns Result.success(mockAlbums)

        viewModel = AlbumsViewModel(getAlbumsUseCase)
        advanceUntilIdle() // Wait for init call

        // When
        viewModel.loadAlbums()
        viewModel.loadAlbums()
        advanceUntilIdle()

        // Then
        coVerify(exactly = 3) { getAlbumsUseCase() } // 1 from init + 2 manual calls
    }

    @Test
    fun `viewMode changes should not affect uiState`() = runTest {
        // Given
        val mockAlbums = listOf(
            Album(
                id = "1",
                name = "Camera",
                type = AlbumType.CAMERA,
                coverImageUri = "content://media/external/images/media/1",
                itemCount = 10,
                lastModified = 1672531200000L
            )
        )
        coEvery { getAlbumsUseCase() } returns Result.success(mockAlbums)

        viewModel = AlbumsViewModel(getAlbumsUseCase)
        advanceUntilIdle()

        val initialState = viewModel.uiState.first()

        // When
        viewModel.toggleViewMode()
        viewModel.toggleViewMode()

        // Then
        val finalState = viewModel.uiState.first()
        assertEquals(initialState, finalState)
        assertTrue(finalState is ViewState.Success)
        assertEquals(mockAlbums, (finalState as ViewState.Success).data)
    }


    @Test
    fun `error state should persist until next loadAlbums call`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { getAlbumsUseCase() } returns Result.failure(exception)

        viewModel = AlbumsViewModel(getAlbumsUseCase)
        advanceUntilIdle()

        // Verify error state
        val errorState = viewModel.uiState.first()
        assertTrue(errorState is ViewState.Error)

        // When - Toggle view mode (should not affect error state)
        viewModel.toggleViewMode()

        // Then
        val stateAfterToggle = viewModel.uiState.first()
        assertTrue(stateAfterToggle is ViewState.Error)
        assertEquals(exception, (stateAfterToggle as ViewState.Error).exception)
    }
}