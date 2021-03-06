package world.augma.ui.services;

import android.widget.ImageView;

import world.augma.asset.User;
import world.augma.ui.main.UIDrawerHeader;

public interface ServiceUIMain {

    User fetchUser();

    void refreshUser();

    ImageView getProfileView();

    ImageView getBackgroundView();

    void updateHeader();
}
