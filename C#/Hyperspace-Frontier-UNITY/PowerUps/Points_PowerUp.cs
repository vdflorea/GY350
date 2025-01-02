using UnityEngine;

public class Points_PowerUp : PowerUp
{
    protected override void ApplyPowerUp()
    {
        // Multiply score multiplier by 2 (double points)
        if (GameManager.instance.scoreMultiplier < 4)
        {
            GameManager.instance.scoreMultiplier *= 2;
        }
    }

    protected override void UpdateUI()
    {
        // Score is in a powered-up state
        if (GameManager.instance.scoreMultiplier > 1)
        {
            GameManager.instance.scoreText.color = Color.green;
            GameManager.instance.scoreTimerIsRunning = true;
            GameManager.instance.scoreMultiplierCountdown = 5f;
        }
        // Score multiplier in debuffed state initially; Now back to normal after collecting power-up
        else if (GameManager.instance.scoreMultiplier == 1)
        {
            GameManager.instance.scoreText.color = Color.white;
            GameManager.instance.scoreTimerIsRunning = false;
            GameManager.instance.scoreMultiplierCountdown = 0f;
        }
    }
}
