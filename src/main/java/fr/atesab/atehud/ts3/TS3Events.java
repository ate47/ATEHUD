package fr.atesab.atehud.ts3;

public enum TS3Events {
	notifytalkstatuschange(
"notifytalkstatuschange"), notifymessage(
"notifymessage"), notifymessagelist(
"notifymessagelist"), notifycomplainlist(
"notifycomplainlist"), notifybanlist(
"notifybanlist"), notifyclientmoved(
"notifyclientmoved"), notifyclientchannelgroupchanged(
"notifyclientchannelgroupchanged"), notifyclientleftview(
"notifyclientleftview"), notifycliententerview(
"notifycliententerview"), notifyclientpoke(
"notifyclientpoke"), notifyclientchatclosed(
"notifyclientchatclosed"), notifyclientchatcomposing(
"notifyclientchatcomposing"), notifyclientupdated(
"notifyclientupdated"), notifyclientids(
"notifyclientids"), notifyclientdbidfromuid(
"notifyclientdbidfromuid"), notifyclientnamefromuid(
"notifyclientnamefromuid"), notifyclientnamefromdbid(
"notifyclientnamefromdbid"), notifyclientuidfromclid(
"notifyclientuidfromclid"), notifyconnectioninfo(
"notifyconnectioninfo"), notifychannelcreated(
"notifychannelcreated"), notifychanneledited(
"notifychanneledited"), notifychanneldeleted(
"notifychanneldeleted"), notifychannelmoved(
"notifychannelmoved"), notifyserveredited(
"notifyserveredited"), notifyserverupdated(
"notifyserverupdated"), channellist(
"channellist"), channellistfinished(
"channellistfinished"), notifytextmessage(
"notifytextmessage"), notifycurrentserverconnectionchanged(
"notifycurrentserverconnectionchanged"), notifyconnectstatuschange(
"notifyconnectstatuschange"), notifyservergrouplist(
"notifyservergrouplist"), notifychannelgrouplist(
"notifychannelgrouplist");

	String name;

	TS3Events(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
