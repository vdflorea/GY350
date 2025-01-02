using UnityEngine;

public class FireRate_PowerUp : PowerUp
{
    protected override void ApplyPowerUp()
    {
        // Divide fire-rate multiplier by 2 (double the bullets per second)
        if (GameManager.instance.fireRateMultiplier > 0.25f)
        {
            GameManager.instance.fireRateMultiplier /= 2;
        }
    }

    protected override void UpdateUI()
    {
        // Fire-rate is in a powered-up state
        if (GameManager.instance.fireRateMultiplier < 1)
        {
            GameManager.instance.fireRateText.color = Color.green;
            GameManager.instance.fireRateTimerIsRunning = true;
            GameManager.instance.fireRateMultiplierCountdown = 5f;
        }
        // Fire-rate multiplier in debuffed state initially; Now back to normal after collecting power-up
        else if (GameManager.instance.fireRateMultiplier == 1)
        {
            GameManager.instance.fireRateText.color = Color.white;
            GameManager.instance.fireRateTimerIsRunning = false;
            GameManager.instance.fireRateMultiplierCountdown = 0f;
        }
    }
}
