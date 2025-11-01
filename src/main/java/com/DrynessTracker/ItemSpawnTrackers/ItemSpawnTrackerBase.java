package com.DrynessTracker.ItemSpawnTrackers;

public abstract class ItemSpawnTrackerBase implements IItemSpawnTracker {

    private int _dryCount;

    @Override
    public String GetGZMessage() {
        return "GZ with the very nice pull.  Resetting count";
    }

    @Override
    public void IncrementDryCount() {
        _dryCount++;
    }

    @Override
    public void ResetDryCount() {
        _dryCount = 0;
    }

    @Override
    public int GetDryCount() {
        return _dryCount;
    }

    @Override
    public void SetDryCount(int value) {
        _dryCount = value;
    }
}
