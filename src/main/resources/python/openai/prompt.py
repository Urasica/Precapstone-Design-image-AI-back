import sys
from openai import OpenAI

import os

file_path = os.path.abspath("./key.txt")

try:
    with open(file_path, "r", encoding="utf-8") as f:
        api_key = f.read().strip()
    client = OpenAI(api_key=api_key)
except FileNotFoundError:
    print(f"File not found: {file_path}")
except PermissionError:
    print(f"Permission denied: {file_path}")
except Exception as e:
    print(f"An error occurred: {e}")

def generate_emoji_description(text):
    try:
        prompt = (
         f"Transform the given concept into brief visual cues for image creation, "
         f"excluding actual symbols and staying within the specified limit: '{text}'."
         f"Avoid full phrases, focusing on key visual elements and attributes."
         f"Exclude any numerical values ​​or written words in the output."
         f"Ensure no human figures, faces, or body parts are included in the description."
         f"Use suggestive phrases instead of color tones"
         )

        response = client.chat.completions.create(
            model="gpt-4o",
            messages=[{"role": "user", "content": prompt}],
            temperature=0.5,
            max_tokens=70
        )

        description = response.choices[0].message.content.replace("\n", " ").strip()
        token_count = len(description.split())

        if token_count > 70:
            prompt = (
                f"Please summarize the previous response strictly under 70 tokens "
                f"without losing essential details."
            )
            response = client.chat.completions.create(
                model="gpt-4o",
                messages=[{"role": "user", "content": prompt}],
                temperature=0.5,
                max_tokens=70
            )
            description = response.choices[0].message.content.replace("\n", " ").strip()

        return description

    except Exception as e:
        print("An error occurred:", e)
        return None

if __name__ == '__main__':
    if len(sys.argv) > 1:
        korean_text = sys.argv[1]
        description = generate_emoji_description(korean_text)
        print(description + "- Make sure the image fills the screen")
