using UnityEngine;

public class FireRate_Debuff : Debuff
{
    protected override void ApplyDebuff()
    {
        // Multiply fire-rate multiplier by 2 (half the bullets per second)
        if (GameManager.instance.fireRateMultiplier < 4f)
        {
            GameManager.instance.fireRateMultiplier *= 2;
        }
    }

    protected override void UpdateUI()
    {
        // Fire-rate is in a debuffed state
        if (GameManager.instance.fireRateMultiplier > 1)
        {
            GameManager.instance.fireRateText.color = Color.red;
            GameManager.instance.fireRateTimerIsRunning = true;
            GameManager.instance.fireRateMultiplierCountdown = 5f;
        }
        // Fire-rate multiplier in powered-up state initially; Now back to normal after collecting debuff
        else if (GameManager.instance.fireRateMultiplier == 1)
        {
            GameManager.instance.fireRateText.color = Color.white;
            GameManager.instance.fireRateTimerIsRunning = false;
            GameManager.instance.fireRateMultiplierCountdown = 0f;
        }
    }
}
