package com.example.memocalendarapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.memocalendarapp.data.MemoDatabase
import com.example.memocalendarapp.ui.MainScreen
import com.example.memocalendarapp.viewmodel.MemoViewModel
import com.example.memocalendarapp.viewmodel.MemoViewModelFactory
import org.junit.Rule
import org.junit.Test

class MainScreenTest {

    // 建立 Compose 測試規則，這是 Compose UI 測試必要的起點
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun floatingActionButton_isVisible() {
        // 1. 建立一個在記憶體內的資料庫 (In-Memory Database)，專門用於測試
        // 這樣測試後資料庫不會留下任何實體檔案或資料痕跡
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val db = Room.inMemoryDatabaseBuilder(context, MemoDatabase::class.java)
            .allowMainThreadQueries()  // 測試環境允許主執行緒訪問資料庫，以便即時驗證結果
            .build()

        // 2. 建立 ViewModelFactory 並利用 factory 產生 MemoViewModel 實例
        // 透過這種方法可避免需要存取私有 (private) 欄位問題
        val factory = MemoViewModelFactory(db.memoDao())
        val viewModel = factory.create(MemoViewModel::class.java)

        // 3. 設定 Compose 畫面的測試內容 (即測試目標 UI 元件)
        composeRule.setContent {
            MainScreen(
                viewModel = viewModel,
                onAddMemo = {},          // 測試時點擊事件可以不用實際作用
                onOpenLocation = {}      // 同上，這裡只關注 FAB 是否顯示
            )
        }

        // 4. 執行測試：「確認畫面上有 FloatingActionButton (用 testTag 標記 "FAB_ADD_MEMO")，且它是可見的」
        composeRule.onNodeWithTag("FAB_ADD_MEMO")
            .assertIsDisplayed() // 斷言 (Assert)：確認該 UI 元件有顯示在畫面上
    }
}
