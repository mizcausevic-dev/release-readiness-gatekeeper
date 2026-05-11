from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "screenshots"
OUT.mkdir(exist_ok=True)

WIDTH = 1440
HEIGHT = 900
BG = "#091321"
PANEL = "#131f31"
CARD = "#1a2940"
BORDER = "#294764"
TEXT = "#f3efe1"
SUB = "#b8c7dc"
ACCENT = "#8bc5ff"
WARN = "#ffd76d"


def font(size: int, bold: bool = False):
    candidates = [
        "C:/Windows/Fonts/seguisb.ttf" if bold else "C:/Windows/Fonts/segoeui.ttf",
        "C:/Windows/Fonts/arialbd.ttf" if bold else "C:/Windows/Fonts/arial.ttf",
    ]
    for candidate in candidates:
        path = Path(candidate)
        if path.exists():
            return ImageFont.truetype(str(path), size)
    return ImageFont.load_default()


TITLE = font(54, True)
SECTION = font(18, False)
BODY = font(26, False)
CARD_TITLE = font(18, False)
CARD_VALUE = font(34, True)
CARD_BODY = font(19, False)


def wrapped(draw, text, xy, wrap_width, font_obj, fill, line_gap=10):
    words = text.split()
    lines = []
    current = ""
    for word in words:
        trial = word if not current else f"{current} {word}"
        if draw.textlength(trial, font=font_obj) <= wrap_width:
            current = trial
        else:
            lines.append(current)
            current = word
    if current:
        lines.append(current)
    x, y = xy
    line_height = font_obj.size + line_gap
    for line in lines:
        draw.text((x, y), line, font=font_obj, fill=fill)
        y += line_height


def shell():
    image = Image.new("RGB", (WIDTH, HEIGHT), BG)
    draw = ImageDraw.Draw(image)
    draw.rounded_rectangle((30, 30, WIDTH - 30, HEIGHT - 30), 28, fill=PANEL, outline=BORDER, width=2)
    return image, draw


def hero():
    image, draw = shell()
    draw.text((90, 82), "RELEASE READINESS GATEKEEPER", font=SECTION, fill=ACCENT)
    wrapped(draw, "Turn launch pressure into a clear ship, watch, or hold decision.", (90, 130), 1160, TITLE, TEXT, 8)
    wrapped(draw, "Freeze windows, dependency drag, error budget, and rollback posture in one Kotlin gate service.", (90, 270), 1060, BODY, SUB, 8)
    cards = [
        ("PENDING RELEASES", "3", "Active launch threads in view"),
        ("HOLD NOW", "1", "Immediate freeze-window escalation"),
        ("WATCH LANES", "1", "Conditional ship posture"),
        ("DEFAULT PORT", "4428", "Friendly local boot path"),
    ]
    x = 90
    for title, value, body in cards:
        draw.rounded_rectangle((x, 360, x + 285, 555), 22, fill=CARD, outline=BORDER, width=2)
        draw.text((x + 24, 388), title, font=CARD_TITLE, fill=SUB)
        draw.text((x + 24, 430), value, font=CARD_VALUE, fill=TEXT)
        wrapped(draw, body, (x + 24, 488), 230, CARD_BODY, SUB, 6)
        x += 305
    draw.rounded_rectangle((90, 620, WIDTH - 90, 800), 24, fill=CARD, outline=BORDER, width=2)
    draw.text((120, 652), "CURRENT DECISION", font=CARD_TITLE, fill="#ffbfdc")
    wrapped(draw, "Hold the checkout runtime release until rollback ownership and dependency clearance are both explicit.", (120, 694), 1120, font(40, True), TEXT, 8)
    image.save(OUT / "01-hero.png")


def lanes():
    image, draw = shell()
    draw.text((90, 82), "DECISION LANES", font=SECTION, fill=ACCENT)
    wrapped(draw, "Release threads should move through clean decision lanes, not vague launch conversations.", (90, 130), 1180, TITLE, TEXT, 8)
    cards = [
        ("HOLD", "Freeze window is active and rollback posture is incomplete.", "Platform release lane"),
        ("WATCH", "Low error budget but rollback is clean enough to stay conditional.", "Identity systems"),
        ("SHIP", "Dependency drag is clear enough for normal monitoring.", "Growth systems"),
    ]
    x = 110
    for title, body, lane in cards:
        draw.rounded_rectangle((x, 300, x + 360, 620), 24, fill=CARD, outline=BORDER, width=2)
        draw.text((x + 28, 332), title, font=font(28, True), fill=ACCENT)
        wrapped(draw, body, (x + 28, 402), 290, font(30, True), TEXT, 8)
        draw.text((x + 28, 542), lane, font=font(22, False), fill=WARN)
        x += 390
    image.save(OUT / "02-decision-lanes.png")


def escalation():
    image, draw = shell()
    draw.text((90, 82), "ESCALATION VIEW", font=SECTION, fill=ACCENT)
    wrapped(draw, "One release, one score, one next action.", (90, 130), 1050, TITLE, TEXT, 8)
    draw.rounded_rectangle((90, 290, 770, 780), 24, fill=CARD, outline=BORDER, width=2)
    draw.text((120, 322), "REL-9401 · CHECKOUT RUNTIME", font=CARD_TITLE, fill=ACCENT)
    draw.text((120, 390), "Status: escalate", font=font(38, True), fill=TEXT)
    draw.text((120, 450), "Decision: hold", font=font(24, False), fill=SUB)
    draw.text((120, 492), "Owner lane: incident-command", font=font(24, False), fill=SUB)
    draw.text((120, 552), "Key risks", font=font(26, True), fill=TEXT)
    wrapped(draw, "Freeze window is active. Error budget is nearly exhausted. Rollback ownership is incomplete.", (120, 596), 560, CARD_BODY, SUB, 6)

    draw.rounded_rectangle((810, 290, WIDTH - 90, 780), 24, fill=CARD, outline=BORDER, width=2)
    draw.text((840, 322), "IMMEDIATE ACTION", font=CARD_TITLE, fill="#ffbfdc")
    wrapped(draw, "Pause the release, assign a rollback owner, and clear dependency drag before the next freeze checkpoint.", (840, 382), 470, font(30, True), TEXT, 8)
    draw.text((840, 560), "Stabilizers", font=font(26, True), fill=TEXT)
    wrapped(draw, "A next-step sequence already exists. Release owner is known. The risk is concentrated, not diffuse.", (840, 604), 470, CARD_BODY, SUB, 6)
    image.save(OUT / "03-escalation.png")


def proof():
    image, draw = shell()
    draw.text((90, 82), "VALIDATION PROOF", font=SECTION, fill=ACCENT)
    wrapped(draw, "Build, test, root route, docs, and sample analysis in one proof layer.", (90, 130), 1180, TITLE, TEXT, 8)
    draw.rounded_rectangle((90, 300, 760, 790), 24, fill="#07101c", outline=BORDER, width=2)
    proof_lines = [
        "> .\\gradlew.bat test",
        "BUILD SUCCESSFUL",
        "> .\\gradlew.bat build",
        "BUILD SUCCESSFUL",
        "",
        "> GET /api/sample",
        "{",
        '  "status": "escalate",',
        '  "releaseDecision": "hold"',
        "}",
    ]
    y = 336
    mono = font(24, False)
    for line in proof_lines:
        draw.text((120, y), line, font=mono, fill="#c8f7a5" if line.startswith(">") else SUB)
        y += 34
    draw.rounded_rectangle((810, 300, WIDTH - 90, 790), 24, fill=CARD, outline=BORDER, width=2)
    draw.text((840, 332), "WHY THIS COUNTS", font=CARD_TITLE, fill=ACCENT)
    wrapped(draw, "The gatekeeper turns release risk into an explicit API decision, which makes it usable as a backend control layer instead of just another dashboard idea.", (840, 390), 470, font(28, True), TEXT, 8)
    image.save(OUT / "04-proof.png")


if __name__ == "__main__":
    hero()
    lanes()
    escalation()
    proof()
