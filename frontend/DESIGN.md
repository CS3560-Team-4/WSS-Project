# WSS — Night Survey

Wilderness Survival System is a board-centered survival game. The Java server owns
the map, resources, movement, hints, trading, vision, levels, and scores. Existing
API URLs, methods, payloads, handlers, and timing remain unchanged.

## Direction and hierarchy

The screen answers: where should I move next, and can my resources sustain it?
The terrain map remains the largest surface, movement is the clearest control
group, and the resource strip stays attached to the board. Guide, hints, vision,
and auto-play are supporting tools. The original pixel sprites provide the game's
identity; no new artwork, sample product data, or decorative dashboard cards.

Three directions were explored for the contrast brief:

| Direction | Treatment | Fit |
| --- | --- | --- |
| Ranger Console | Navy throughout, warm-white type, tonal controls and information | Cohesive, but resource information has less separation |
| Field Note Contrast | A broad warm-white play surface with navy framing | Clear reading, but the light plane competes with the map |
| Night Survey | Open navy canvas, warm-white resource strip and dialogs, periwinkle movement | Selected: strong separation, immediate movement, and an unmistakable map focus |

Taste reference: `true-black-flat`, seed `2053109783`. Its shared open ground,
flat surfaces, and lightness-based separation are useful here. The user's navy,
warm-white, and periwinkle roles supersede the library's black-only ground and
neutral primary. Impeccable guides semantic states, readable contrast, and phone
coverage; Emil guides immediate movement and restrained dialog feedback.

## Color system

| Color | Role |
| --- | --- |
| Midnight navy `#101820` | Main canvas; text on light surfaces and primary controls |
| Warm white `#F5F2EA` | Main text; resource strip and dialog surfaces |
| Soft periwinkle `#A5B4FC` | Movement, primary actions, selected controls, dark-surface focus |
| Coral `#FF8A75` | Destructive actions and danger on navy |
| Mint `#72D6C9` | Destination emphasis and positive meaning on navy |

Semantic tokens live in `src/css/index.css`. Light surfaces scope their own ink,
muted, border, focus, and status roles. Dark periwinkle `#3C4E9B`, coral
`#A6332E`, and mint `#17675E` preserve color meanings on warm white while keeping
text readable. Health is coral, water periwinkle, and energy mint; resource names
and numeric values accompany every meter. Gold and scores stay neutral.

Primary controls always use navy text on periwinkle. Hover `#BDCAFF` and pressed
`#91A3EC` keep the same hue family. Secondary actions use neutral surfaces and
visible control borders; selected modes retain a marker, border, and state word.
Disabled controls use an opaque neutral fill and readable muted text. Errors use
the danger role. Focus uses a clear outline, with navy on light surfaces and a
warm-white ring around movement controls. Scrollbars and selection follow the
same palette. No gradients, glow, backdrop blur, or ambient motion.

## Type, layout, and interaction

Instrument Sans owns headings and reading text. IBM Plex Mono has one role:
scores, coordinates, and resource figures. Fonts are self-hosted under
`public/fonts`, with their SIL Open Font Licenses; earlier serif assets are kept
but unused. Original artwork is reused; no external font requests or stock images.

Open side tools flank the board on desktop and tablet. Scores share the compact
header, and the map key sits beside the coordinates above the board. The board
width follows both available column width and viewport height, leaving room for
resources, auto-play status, and the footer. Phone puts the map first, movement
next, and tools last; narrow or short screens retain natural scrolling so content
and controls remain reachable. Dismissible dialogs share a title-and-close header
above their scrolling content. Their scroll padding contains focus outlines. Movement
responds immediately without an animated hover displacement. Existing short
dialog arrival and hint feedback honor reduced motion.

## Preservation boundaries

Game handlers, request wiring, and `Map.css` remain unchanged by layout refinements.
The terrain colors, tile marks, fog opacity, sprites, and legend swatch fills stay
the same. The map frame retains local `--surface: #E3E9ED` and `--line: #CBD4DA`:
the 40% fog opacity must composite against that same backing. Loading text has its
own readable dark ink on the map frame.

Existing auto-play timers, score refresh behavior, hint-gold refresh behavior,
and fog visibility conditions remain as implemented. Only dialogs that already
expose a close button support Escape dismissal.

Displayed coordinates are one-indexed; the server's coordinates remain zero-indexed.
Gold has a yellow coin icon after its numeric value, using fill `#EFC34E` and
outline `#806000` on the warm-white resource strip. Its text stays navy and the
icon is decorative alongside the explicit Gold label.
