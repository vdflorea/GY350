using UnityEngine;

public class Speed_PowerUp : PowerUp
{
    protected override void ApplyPowerUp()
    {
        // Divide speed multiplier by 2 (half speed)
        if (GameManager.instance.speedMultiplier > 0.25)
        {
            GameManager.instance.speedMultiplier /= 2;
        }
    }

    protected override void UpdateUI()
    {
        // Speed is in a powered-up state
        if (GameManager.instance.speedMultiplier < 1)
        {
            GameManager.instance.speedText.color = Color.green;
            GameManager.instance.speedTimerIsRunning = true;
            GameManager.instance.speedMultiplierCountdown = 5f;
        }
        // Speed multiplier in debuffed state initially; Now back to normal after collecting power-up
        else if (GameManager.instance.speedMultiplier == 1)
        {
            GameManager.instance.speedText.color = Color.white;
            GameManager.instance.speedTimerIsRunning = false;
            GameManager.instance.speedMultiplierCountdown = 0f;
        }
    }
}
