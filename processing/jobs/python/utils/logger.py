# utils/logger.py
import logging

def get_logger(name="ETL"):
    logger = logging.getLogger(name)

    if logger.handlers:
        return logger  # éviter les doublons

    logger.setLevel(logging.INFO)

    handler = logging.StreamHandler()
    handler.setFormatter(logging.Formatter('%(asctime)s - %(levelname)s - %(message)s'))

    logger.addHandler(handler)

    return logger