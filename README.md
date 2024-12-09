# Milestone 1 - NBA SnapShot (Unit 7)

## Table of Contents

1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)

## Overview

### Description

The app is a comprehensive companion for NBA fans, offering real-time updates and detailed statistics for teams, players, and games. Users can track live game scores, view player and team season stats, and monitor league standings with seamless navigation. Personalized features like favorite team and player tracking, push notifications for key moments, and live game insights ensure users stay connected. Designed for both casual fans and stat enthusiasts, the app provides an engaging experience with interactive visuals and timely updates. With its user-friendly interface and real-time functionality, it’s the ultimate tool for staying informed during the NBA season.

### App Evaluation

[Evaluation of your app across the following attributes]
- **Category:** Sports & Entertainment
- **Mobile:** Real time stats push notifications for live game updates and breaking news. Use location to tailor content for regional teams. More tailored than ESPN, making a more personalized assistant for basketball fans and people who want to get into the sport.
- **Story:** Become the ultimate basketball fan, get live updates, deep insights and personalized notifications about your favorite teams and players all in one place. Saves time by sonsolidating stats, updates, and team/player performance in one app. 
- **Market:** The NBA has a massive following with millions of fans all over the globe. Potential niche audience in the fantasy basketball community, sports analysts, or gamblers who want live updates on their bets.
- **Habit:** Daily use during the NBA season, especially during live games. Offseason engagement through player stats analysis, trade rumors, and draft news. Engagement opportunities via predictions and social sharing of stats.
- **Scope:** MVP Features: live game tracking with scores, push notifications for updates, simple player and team profiles with season stats, league standings and schedule display. Technical Feasibility: use APIs to pull in stats and data, build simple UI to display. Scable Features: advanced analytics like shot charts and player heatmaps, and fantasy basketball integration or real-time betting odds.

## Product Spec

### 1. User Features (Required and Optional)

**Required Features**

1. User can register and login
2. Display live games and statistics
3. Display standings
4. Display team and player stats

**Optional Features**

1. Search Bar for in player stats, to get a specific player
2. Notifications on player milestones (30 point games, triple doubles, etc.)

### 2. Screen Archetypes

- Home Screen/Live Games
  - [list associated required story here] User can quickly glance over scores and check game progress
  - User can tap on a game to view detailed real-time stats (e.g., points, assists, rebounds) for both teams
  - Push notification during key moments and when games starts 
  - Game status indicators to know which games are active
- Player Stats (for the season)
  - Search for a player by name to view their individual stats for the current season
  - See player's key stats (e.g., points per game, assists, rebounds, shooting percentage) displayed in an easy to read format
- Team Stats (for the season)
  - View aggregated stats for a specific team, including averages for points, rebounds, assists, and shooting percentages.
  - Bookmark my favorite teams for quick access to their stats.
- Standings
  - See the current standings for all NBA teams, organized by conference and division.
  - View additional metrics for teams in the standings, such as win-loss streaks, games behind, and home/away records.
  - Tap on a team in the standings to view their season stats.

### 3. Navigation

**Tab Navigation** (Tab to Screen)

- Bottom Bar Navigation
  - Home Screen
  - Player Stats (for the season)
  - Team Stats (for the season)
  - Standings

**Flow Navigation** (Screen to Screen)

- Home Screen/Live Games
  - Live Game Stats
- Live Game Stats
  - Player Stats (for the game)
  - Team Stats (for the game)
- Home Screen/Live Games
  - Live Game Stats
- Team Stats (for the season)
  - Roster
  - Player Stats (for the season)
- Standings
  - Team Stats


## Wireframes
<img src="https://github.com/user-attachments/assets/a5097805-6497-41e4-a6c0-55c767768984" width=600>

# Milestone 2 - Build Sprint 1 (Unit 8)

## GitHub Project board

<img src="https://github.com/user-attachments/assets/e24ab9fc-7c3d-427e-8498-f64889da416c" width=600>

## Issue cards

- <img src="https://github.com/user-attachments/assets/4296a9cf-06eb-451f-a705-6e21e38382a5" width=600>
- <img src="https://github.com/user-attachments/assets/d210cf9e-6852-4af0-a367-3506060246e1" width=600>

## Issues worked on this sprint

- Completed Team Stats, and Team Detail View (UI needs fixing but data loaded in properly, slow load due to not wanting to have to ping the api 30 consecutive times without error)
- Live Game View started, but not yet completed
- Games from 12/01/24 displayed gif taken at 6:18 pm, not all games have started
- ![NBASnapShot](https://github.com/user-attachments/assets/bfbf7608-9fdc-4757-b44c-a7bc6d0a359f)


<br>

# Milestone 3 - Build Sprint 2 (Unit 9)

## GitHub Project board

 <img src="(https://github.com/user-attachments/assets/cc963c26-8ac6-44a4-95d1-71beefd4babb" width=600>

## Completed user stories

- Added a details activity for the games where it shows the stat leaders for each team.
- Game view shows quarter, game clock and the location of the game.
- Under the team details view added a recycler view to display the players on the teams.
- Better formated the UI to enhance the user experience.
- I originally wanted to make an activity where the user would be able to see a player's stats by clicking on their profile in the list, but I didn't have an API where the current season's data was loaded into.
- I wished to add the option for users to also add players to their followed list, so that they could keep up with their performances, but with no stats for each individual it didn't really make much sense for now.

[Add video/gif of your current application that shows build progress]
<img src="https://github.com/user-attachments/assets/878215e4-d87f-4065-8071-aa9e1c80d263" width=600>


## App Demo Video

- [Embed the YouTube/Vimeo link of your Completed Demo Day prep video](https://youtube.com/shorts/arp9LDqFtoA?feature=share)
