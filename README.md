# TerraParties ( Legacy )
Simple and lightweight plugin, that allows your players to create and join parties. 

## Features
- Teleport to party members
- Party invites, and passwords
- Party MOTD
- JSON data storing
- PermissionsEx chat prefixes/suffixes support

## Available Commands
| Command            | Arguments           | Action 
|--------------------|---------------------|--------------------------------------------------------------------------------------|
| /party create      | party_name          | Creates party named with party_name
| /party join        | party_name password | Join party, specify password to join private party by password
| /party leave       | -                   | Leaves current party
| /party list        | -                   | Show all parties list / Show current party players
| /party invite      | nick                | Invite player to your party
| /party tp          | nick                | Tepeports to player in your party
| /pc                | -                   | Toggles party chat
| /party disband     | -                   | Disbands your party
| /party kick        | nick                | Kicks player from party
| /party status      | -                   | Show current party settings
| /party setpassword | -                   | Sets party password! Warning! Password stored unencrypted!
| /party setinvite   | -                   | Allow players to invite another players
| /party setmotd     | text                | Set party motd, that will show when player enter the server
| /party settype     | -                   | Toggles party private\public state.
| /party setpvp      | -                   | Toggles party PvP mode.
| /party setleader   | nick                | Set new party leader

## Compiling And Running Requirements:
>- Spigot (1.12+)
>- Google/GSON Lib (2.6.2+)
>- PermisionsEx Plugin
