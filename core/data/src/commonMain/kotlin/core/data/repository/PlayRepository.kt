package core.data.repository

import core.models.local.Play


class PlayRepository(
) {
  fun getPlayDataList() : List<Play>{
    return listOf(
      Play(
        id = "05be5f6g78h",
        dateTime = "1 minute ago",
        wager = +7.0f,
        desc = "shifted 6.1 and subtracted two",
        achievement = "8,456 XP"
      ),
      Play(
        id = "a1b2c3d4e5f",
        dateTime = "5 minutes ago",
        wager = -5.5f,
        desc = "flipped twice and gained momentum",
        achievement = "6,123 XP"
      ),
      Play(
        id = "f9e8d7c6b5a",
        dateTime = "10 minutes ago",
        wager = -2.2f,
        desc = "lost after delay, missed trigger",
        achievement = "3,678 XP"
      ),
      Play(
        id = "112233445566",
        dateTime = "20 minutes ago",
        wager = +10.0f,
        desc = "double wager, no shift",
        achievement = "10,789 XP"
      ),
      Play(
        id = "77889900aabb",
        dateTime = "30 minutes ago",
        wager = +3.3f,
        desc = "shifted 1.5 and rebounded",
        achievement = "5,432 XP"
      )
    )

  }
}
