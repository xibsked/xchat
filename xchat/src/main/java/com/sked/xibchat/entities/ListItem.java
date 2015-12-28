package com.sked.xibchat.entities;

import com.sked.xibchat.xmpp.jid.Jid;

import java.util.List;

public interface ListItem extends Comparable<ListItem> {
    String getDisplayName();

    Jid getJid();

    List<Tag> getTags();

    boolean match(final String needle);

    final class Tag {
        private final String name;
        private final int color;

        public Tag(final String name, final int color) {
            this.name = name;
            this.color = color;
        }

        public int getColor() {
            return this.color;
        }

        public String getName() {
            return this.name;
        }
    }
}
