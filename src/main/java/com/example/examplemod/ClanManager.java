package com.example.examplemod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClanManager {
    private static final ClanManager INSTANCE = new ClanManager();

    private final Map<String, List<UUID>> clans = new HashMap<String, List<UUID>>();
    private final Map<UUID, UUID> pendingInvitations = new HashMap<UUID, UUID>();

    private ClanManager() {
    }

    public static ClanManager getInstance() {
        return INSTANCE;
    }

    public synchronized boolean createClan(String name, UUID leader) {
        if (clans.containsKey(name) || getClanName(leader) != null) {
            return false;
        }

        List<UUID> members = new ArrayList<UUID>();
        members.add(leader);
        clans.put(name, members);
        return true;
    }

    public synchronized boolean dissolveClan(UUID player) {
        String clanName = getClanName(player);

        if (clanName == null) {
            return false;
        }

        clans.remove(clanName);
        return true;
    }

    public synchronized boolean addPlayerToClan(String clanName, UUID player) {
        List<UUID> members = clans.get(clanName);

        if (members == null || getClanName(player) != null) {
            return false;
        }

        members.add(player);
        return true;
    }

    public synchronized String getClanName(UUID player) {
        for (Map.Entry<String, List<UUID>> entry : clans.entrySet()) {
            if (entry.getValue().contains(player)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public synchronized void addPendingInvitation(UUID sender, UUID target) {
        pendingInvitations.put(sender, target);
    }

    public synchronized UUID getPendingInvitationTarget(UUID sender) {
        return pendingInvitations.get(sender);
    }

    public synchronized void removePendingInvitation(UUID sender) {
        pendingInvitations.remove(sender);
    }
}
