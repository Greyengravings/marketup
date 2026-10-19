from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    app_name: str = "MarketUp API"
    database_url: str = "sqlite:///./marketup.db"

    # JWT
    jwt_secret_key: str = "CHANGE_ME_IN_PRODUCTION"
    jwt_algorithm: str = "HS256"
    access_token_expire_minutes: int = 15
    refresh_token_expire_days: int = 30

    # AI
    anthropic_api_key: str = ""
    anthropic_model: str = "claude-sonnet-4-6"

    class Config:
        env_file = ".env"


settings = Settings()
