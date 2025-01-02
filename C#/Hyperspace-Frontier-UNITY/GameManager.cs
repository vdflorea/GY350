using UnityEngine;
using TMPro;
using UnityEngine.SceneManagement;

public class GameManager : MonoBehaviour
{
    public static GameManager instance;
    [SerializeField] private GameObject boundary;
    [SerializeField] private GameObject spaceship;
    [HideInInspector] public Spaceship spaceshipScript;

    [HideInInspector] public bool hasSpaceshipTakenOff;
    [HideInInspector] public bool timeToPlay;
    [HideInInspector] public bool startParticles;
    [HideInInspector] public bool isGameOver;

    public GameObject menuUI;
    
    public GameObject playingUI;
    public TMP_Text scoreText;
    public TMP_Text highScoreText;
    public TMP_Text speedText;
    public TMP_Text fireRateText;
    
    [HideInInspector] public float scoreMultiplier = 1;
    [HideInInspector] public float scoreMultiplierCountdown;
    [HideInInspector] public bool scoreTimerIsRunning;
    
    [HideInInspector] public float speedMultiplier = 1;
    [HideInInspector] public float speedMultiplierCountdown;
    [HideInInspector] public bool speedTimerIsRunning;
    
    [HideInInspector] public float fireRateMultiplier = 1;
    [HideInInspector] public float fireRateMultiplierCountdown;
    [HideInInspector] public bool fireRateTimerIsRunning;
    
    [HideInInspector] public int waveNumber;
    private int _score;
    
    private void Awake() 
    {
        instance = this;
    }

    private void Start()
    {
        spaceshipScript = spaceship.GetComponent<Spaceship>();
        ShowMenu();
    }

    public void ShowMenu()
    {
        // Ensure cursor can be moved in menu
        Cursor.visible = true;
        Cursor.lockState = CursorLockMode.None;
        
        // Delete any boundaries/objects left over from previous game
        GameObject[] boundaries = GameObject.FindGameObjectsWithTag("Boundary");
        foreach (GameObject b in boundaries)
        {
            Destroy(b);
        }
        
        // Reset ObjectSpawner static variables
        ObjectSpawner.Reset();
        
        // Manage sounds within menu
        SoundManager.instance.LoadSoundSettings();
        SoundManager.instance.StopHyperspaceSound();
        SoundManager.instance.PlayMenuMusic();
        
        isGameOver = false;
        
        // Set Menu UI as active UI
        menuUI.SetActive(true);
        playingUI.SetActive(false);
    }
    
    public void ShowPlaying()
    {
        // Ensure cursor is locked & hidden while playing
        Cursor.lockState = CursorLockMode.Locked;
        Cursor.visible = false;
        
        isGameOver = false;
        
        // Execute initial steps to transition to playing state
        SoundManager.instance.StopMenuMusic();
        SpawnBoundary();
        
        // Set Playing UI as active UI
        menuUI.SetActive(false);
        playingUI.SetActive(true);
    }
    
    public void QuitGame()
    {
        Application.Quit();
    }
    
    public void RestartGameAfterDelay(float delayInSeconds)
    {
        Invoke("ReloadScene", delayInSeconds);
    }
    
    private void ReloadScene()
    {
        SceneManager.LoadScene(SceneManager.GetActiveScene().buildIndex);
    }

    private void SpawnBoundary()
    {
        Instantiate(boundary, new Vector3(0, 0, 0), Quaternion.identity);
    }
    
    public void UpdateScore()
    {
        _score += (int)(100 * scoreMultiplier);
        
        // Check and update high score
        int currentHighScore = PlayerPrefs.GetInt("HighScore", 0);
        if (_score > currentHighScore)
        {
            PlayerPrefs.SetInt("HighScore", _score);
            PlayerPrefs.Save();
        }
    }

    private void Update()
    {
        UpdateScoreText();
        UpdateSpeedText();
        UpdateFireRateText();
    }

    private void UpdateScoreText()
    {
        if (scoreTimerIsRunning)
        {
            // Player picked up score powerup/debuff (reset countdown)
            if (scoreMultiplierCountdown > 0)
            {
                scoreMultiplierCountdown -= Time.deltaTime;
                scoreText.text = "Score: " + _score + " " 
                                 + DisplayTime(scoreMultiplierCountdown) + "s"
                                 + " (" + scoreMultiplier + "x)";
            }
            else
            {
                // Countdown finished, back to normal score multiplier
                scoreTimerIsRunning = false;
                scoreText.color = Color.white;
                scoreMultiplier = 1;
            }
        }
        else
        {
            scoreText.text = "Score: " + _score;
        }
        
        // Set (potentially new) high-score
        highScoreText.text = "High Score: " + PlayerPrefs.GetInt("HighScore", 0);
    }

    private void UpdateSpeedText()
    {
        if (speedTimerIsRunning)
        {
            // Player picked up speed powerup/debuff (reset countdown)
            if (speedMultiplierCountdown > 0)
            {
                speedMultiplierCountdown -= Time.deltaTime;
                speedText.text = "Speed: " + (spaceshipScript.speed*speedMultiplier) + "km/s " 
                                 + DisplayTime(speedMultiplierCountdown) + "s"
                                 + " (" + speedMultiplier + "x)";
            }
            else
            {
                // Countdown finished, back to normal speed multiplier
                speedTimerIsRunning = false;
                speedText.color = Color.white;
                speedMultiplier = 1;
            }
        }
        else
        {
            speedText.text = "Speed: " + spaceshipScript.speed + "km/s";
        }
    }

    private void UpdateFireRateText()
    {
        if (fireRateTimerIsRunning)
        {
            // Player picked up fire-rate powerup/debuff (reset countdown)
            if (fireRateMultiplierCountdown > 0)
            {
                fireRateMultiplierCountdown -= Time.deltaTime;
                fireRateText.text = "Fire Rate: " + ((1f / (spaceshipScript.fireRate*fireRateMultiplier))*2) + "b/s "
                                    + DisplayTime(fireRateMultiplierCountdown) + "s"
                                    + " (" + (1/fireRateMultiplier) + "x)";
            }
            else
            {
                // Countdown finished, back to normal fire-rate multiplier
                fireRateTimerIsRunning = false;
                fireRateText.color = Color.white;
                fireRateMultiplier = 1;
            }
        }
        else
        {
            fireRateText.text = "Fire Rate: " + ((1f / spaceshipScript.fireRate)*2) +"b/s";
        }
    }

    private string DisplayTime(float time)
    {
        // Ensure time doesn't become negative
        time = Mathf.Max(0, time);
        
        // Round to one decimal place
        float seconds = Mathf.Round(time * 10f) / 10f;
        
        // Display (to one decimal place)
        return seconds.ToString("F1");
    }

    public void TakeOff()
    {
        // Start the game after a delay to illustrate
        // spaceship "taking off"
        hasSpaceshipTakenOff = true;
        Invoke("GameStart", 2f);
    }

    public void StartHyperspaceEffect()
    {
        startParticles = true;
    }

    private void GameStart()
    {
        timeToPlay = true;
        SoundManager.instance.PlayHyperspaceSound();
    }
}
