# 📝 NoteApp

**Kotlin** とモダンな Android アーキテクチャコンポーネントで構築した、シンプルで洗練されたメモアプリです。NoteApp は「理解しやすく、拡張しやすい」をコンセプトにした、堅牢な土台として設計されています。

---

## ✨ 機能一覧

| 機能 | 詳細 |
|---|---|
| 🎉 ウェルカム画面 | 起動時に全画面で表示されるスプラッシュ。画面をタップするとアプリに入れる |
| 📋 ノート一覧 | 作成日時の新しい順にノートをスクロールリストで表示 |
| ➕ ノート作成 | タイトルと本文を入力して新しいノートを追加 |
| ✏️ ノート編集 | 一覧からノートをタップして内容を編集 |
| 🗑️ ノート削除 | 編集画面からノートを削除 |
| ✅ 入力バリデーション | タイトルが空の場合はわかりやすいエラーメッセージを表示 |
| 💾 永続ストレージ | Room によりアプリを再起動してもノートが消えない |
| 🎨 Material Design 3 | Material You デザインシステムに準拠。ダークモード対応 |

---

## 🏗️ アーキテクチャ

NoteApp は **MVVM（Model-View-ViewModel）** パターンを採用し、各レイヤーを明確に分離しています。

```
app/
└── data/
│   ├── model/          # Note エンティティ（Room @Entity）
│   ├── local/          # Room DAO + Database
│   └── repository/     # NoteRepository — 唯一のデータ参照元
└── ui/
    └── notes/          # 各画面の Fragment + ViewModel
```

### 各レイヤーの責務

- **Model** (`Note.kt`) — Room アノテーションを付けたシンプルなデータクラス
- **DAO** (`NoteDao.kt`) — `suspend fun` と `Flow` で公開された SQL クエリ
- **Repository** (`NoteRepository.kt`) — UI からデータソースを隠蔽する抽象層
- **ViewModel** (`NoteListViewModel`, `NoteEditViewModel`) — UI の状態を保持し、画面回転に耐える
- **Fragment** — ViewModel を監視して UI を更新する。ビジネスロジックは持たない

---

## 🛠️ 技術スタック

| ライブラリ | バージョン | 用途 |
|---|---|---|
| Kotlin | 2.1.20 | 主要言語 |
| Room | 2.7.0 | ローカル SQLite 永続化 |
| Navigation Component | 2.9.7 | Fragment 間のナビゲーション・バックスタック管理 |
| Safe Args | 2.9.7 | 型安全なナビゲーション引数 |
| Kotlin Coroutines | 1.10.2 | 非同期処理 |
| Kotlin Flow | — | リアクティブなノートデータストリーム |
| ViewModel / LiveData | 2.10.0 | ライフサイクル対応の UI 状態管理 |
| View Binding | — | null 安全なビューアクセス |
| Material Design 3 | 1.12.0 | UI コンポーネントとテーマ |
| RecyclerView | 1.4.0 | ノート一覧の効率的な描画 |

---

## 🔍 このアプリの特徴

### 1. リアクティブなデータパイプライン
Room データベースから Kotlin `Flow` を通じて UI まで、データが自動的に流れます。ノートを保存・削除したとき、一覧は手動更新なしで即座に反映されます。

```kotlin
// NoteDao.kt
@Query("SELECT * FROM notes ORDER BY createdAt DESC")
fun getAll(): Flow<List<Note>>
```

### 2. Safe Args による型安全なナビゲーション
画面間の引数は生の `Bundle` 文字列ではなく、型付きオブジェクトとして渡されます。ミスをランタイムではなくコンパイル時に検出できます。

```kotlin
// NoteListFragment.kt
val action = NoteListFragmentDirections
    .actionNoteListFragmentToNoteEditFragment(note.id.toInt())
findNavController().navigate(action)
```

### 3. スレッドセーフなシングルトン Room データベース
`@Volatile` と double-checked `synchronized` ブロックを使用したデータベースインスタンス管理で、マルチスレッド環境での競合状態を防ぐプロダクションレディなパターンを採用しています。

### 4. シングル Activity アーキテクチャ
`MainActivity` はひとつだけ。すべてのナビゲーションを Navigation Component が担います。`FragmentTransaction` のボイラープレートが一切不要で、バックスタックも自動管理されます。

### 5. リリース向けビルド設定済み
リリースビルドはコード圧縮（`isMinifyEnabled = true`）とリソース圧縮（`isShrinkResources = true`）が最初から有効化されています。ストア公開前に追加設定は不要です。

---

## 🚀 はじめ方

### 動作要件
- Android Studio Hedgehog 以降
- Android SDK 24 以上（Android 7.0 Nougat 以上）
- JDK 17

### 実行手順
1. プロジェクトをクローンまたはダウンロード
2. Android Studio で開く
3. Gradle の同期が完了するまで待つ
4. エミュレーターまたは実機（API 24 以上）で実行

---

## 🗺️ ロードマップ

プロジェクトには 18 個の機能を 4 フェーズに分けた [`NoteApp-Feature-Roadmap.md`](NoteApp-Feature-Roadmap.md) が同梱されています。

| フェーズ | 主な機能 |
|---|---|
| 🟢 すぐできる | スワイプ削除・アンドゥ・ノート共有・文字数カウント・更新日時 |
| 🟡 中程度 | 全文検索・並び替え・ピン留め・カラーラベル・アーカイブ・ダークモード |
| 🔴 高度な機能 | リマインダー・ゴミ箱・Markdown 表示・エクスポート/バックアップ |
| 🔮 仕上げ | ホーム画面ウィジェット・生体認証ロック・Hilt による DI |

---

## 📁 プロジェクト構成

```
NoteApp/
├── app/src/main/
│   ├── java/com/example/noteapp/
│   │   ├── data/
│   │   │   ├── local/        NoteDao.kt, NoteDatabase.kt
│   │   │   ├── model/        Note.kt
│   │   │   └── repository/   NoteRepository.kt
│   │   ├── ui/notes/
│   │   │   ├── WelcomeFragment.kt
│   │   │   ├── NoteListFragment.kt + NoteListViewModel.kt
│   │   │   ├── NoteEditFragment.kt + NoteEditViewModel.kt
│   │   │   └── NoteAdapter.kt
│   │   └── MainActivity.kt
│   └── res/
│       ├── layout/           fragment_welcome.xml, fragment_note_list.xml, ...
│       ├── navigation/       nav_graph.xml
│       └── values/           strings.xml, themes.xml, colors.xml
├── NoteApp-Feature-Roadmap.md
├── README.md
└── README_JA.md
```

---

## 📄 ライセンス

このプロジェクトは学習・実験・個人利用を目的として自由にご利用いただけます。

