# Redesign verification

Run from `/home/dxle/builds/wss-game/frontend` on 2026-10-20.

- `npm run build`: passed.
- `npm run lint`: zero errors; seven existing App.jsx hook dependency warnings.
- `git diff --check`: passed.
- Direct comparison confirms App's orchestration matches HEAD after excluding
  presentation imports and two unused bindings. Fetch URLs, methods, bodies,
  state updates, modal triggers, keyboard guards, and both autoplay timers remain.
- GPT-6 Luna independently reviewed the map predicates, UI callbacks, resource
  values, guide facts, hint cost, affordability gating, and result flows.

## Browser evidence

Chromium at 1440, 768, and 390 px, served at the backend's allowed origin
`http://localhost:5173`.

Live Java backend: board loads; welcome blocks keyboard movement; D-pad and
keyboard issue the original move request and update coordinates; all four vision
POSTs refresh the map. Sprite loads, page and dialog overflow, separate D-pad
positions, guide top/bottom scrolling, focus restoration, focus trapping, reduced
motion, and the insufficient-gold hint state were checked. No browser exceptions.

Controlled network responses exercise all three hint results and loading states,
all autoplay modes and stop, trade accept/reject and insufficient-gold gating,
protected trader dismissal, death/reset, movement blocking after death, and
victory/next-level. These are frontend interaction proofs; they do not certify
those backend algorithms end to end. Fixture data is confined to the external
verification harness, not the application.

Screenshots and request evidence: `/tmp/wss-verification/`.
Harness: `/tmp/wss-check.mjs` (uses the installed skill's CDP client).

## Design tools

Taste draw: editorial-greeting, seed 976239987, adapted to the lo-fi game brief.
Taste source lint: zero failures. Impeccable detector completed. Taste inspect
completed at all three widths; additional probes ran after Start Game.

The generic inspector flags sprites on terrain as boxed icons and reports the
pixel board as an empty region. Its first-load pass also flags the welcome
dialog's intentional overlay/shadow. These are not suppressed; rendered images
were reviewed directly. Warm paper, the serif heading, compact labels, and a
third face confined to measurements are deliberate choices for this brief.
Contrast and sub-11px text findings were fixed. The automated report is not a
blanket visual pass.

Existing package manifest/lockfile changes and backend Maven metadata changes
were preserved. No backend source files were changed.

## Lo-fi blue refinement

The follow-up adds faded denim actions, pale blue resource/control and dialog
panels, blue-gray ink, and coherent blue focus/hover/selected states. Warm paper,
all terrain colors, semantic health/energy/gold colors, layout, and type remain.
frontend-design informed palette restraint; triple-threat's polish workflow and
GPT-6 Luna's read-only palette audit informed control/state consistency.

Only CSS and design documentation changed in this pass. SHA-256 checks confirm
App.jsx and every component JSX file match their pre-refinement contents exactly.
Build and lint pass (the same seven hook warnings); diff whitespace checks pass.
Core body, secondary, accent, primary-button, and selected-button color pairs
measure at least 4.5:1; the hint label uses deeper blue on its blue panel.

Chromium renders were reviewed at 1440, 768, and 390 px, including the welcome,
hint result, trade, and scrolled guide. Controlled-response checks passed for
all hints/autoplay modes, trade gating, death/reset, victory/next level, focus
restoration, scrolling, and page/dialog overflow. No browser exceptions occurred.
The Java backend was unavailable during this refinement; these checks used the
previous real-state snapshot via external CDP request interception. No fixtures
or request interception were added to the application, and no new live API
verification is claimed.

Evidence and harness: `/tmp/wss-blues/`. Taste lint has zero failures and the
existing 16 warnings. Taste inspect and post-welcome probes retain the existing
sprite/icon, deliberate overlay, cream, type, title-size, and board-as-blank-area
heuristics. These were reviewed against actual screenshots; they are not a
blanket automated visual pass. No contrast or horizontal-overflow defects were
reported by the post-welcome probes.

## Blue canvas refinement

The next refinement makes `#b4c6d5` dusty blue the page background and fits the
components to it: lighter blue panels/dialogs, sage primary actions and active
auto-play, muted red warnings/rejection/reset, and orange vision/level/gold/trade
details. frontend-design informs the dominant palette and restrained secondary
accents; triple-threat's polish workflow informs state and responsive coverage.
GPT-6 Luna supplied a read-only CSS/state audit.

Only CSS and design documentation changed. All component JSX, App.jsx, and
Map.css pass their pre-pass SHA-256 comparisons. Every tile's computed background,
mark color, and opacity match before/after. The map backing and border also
match; a pixel comparison of the rendered board interior is identical. The board
locally retains its old backing tokens so fog blending stays unchanged.

Build passes, lint has zero errors and the same seven warnings, and whitespace
checks pass. Eleven representative text/button palette pairs meet 4.5:1 (minimum
4.62:1). Chromium captures at 1440/768/390 were reviewed, plus welcome, hint,
trade, and scrolled guide states. The controlled-response hint/autoplay/trade/
death/victory suite passes with no exceptions or horizontal overflow. This pass
also uses external response interception because the Java backend is offline;
it adds no application fixtures and makes no new live API claim.

Evidence, comparisons, contrast values, screenshots, and external harnesses are
in `/tmp/wss-blue-bg/`. Taste lint reports zero failures and 16 existing warnings.
Taste inspect retains intentional overlay/type/board-icon findings and flags
control/panel borders as stronger than its generic preference. Post-welcome probes
report no contrast or overflow defects. The inspector's empty-board finding on
the un-intercepted run comes from the unavailable backend; fixture captures show
the actual board. Automated heuristic findings do not certify visual quality.


## Night Survey contrast redesign

Explored Ranger Console, Field Note Contrast, and Night Survey; chose Night
Survey for a strong board/movement hierarchy. Taste's flat dark-surface reference
is `true-black-flat`, seed `2053109783`, adapted to the user's explicit navy,
warm-white, periwinkle, coral, and mint roles. GPT-6 Luna supplied independent
read-only direction and state/contrast reviews. Durable choices are in DESIGN.md.

This pass changes CSS and design evidence only. SHA-256 comparisons confirm all
JSX (including App, Map, and API/game handlers) and Map.css match the start of
this pass. All 121 tiles retain their computed background, mark color, and
opacity; the board backing/border and guide swatch fills are unchanged. The
header/type geometry changes the board's raster position. Full board-image
pixel identity is not claimed; some fog samples differ by one RGB level from
raster/compositing rounding despite identical computed colors and backing.

Production build passes. ESLint has zero errors and the same seven existing
App hook warnings; git diff whitespace checks pass. Browser checks cover
1440/768/390 px with no horizontal overflow or browser exceptions. Rendered
main screens, welcome/focus, guide and its bottom scroll, vision, brain result,
trade, insufficient-gold trade, death, and victory were opened and reviewed.
Focus padding and close-control clearance were corrected in scrollable dialogs.

55 computed samples cover normal, hover, pressed, selected, focus, disabled,
and error colors, plus resource-meter boundaries. Minimum text contrast is
5.39:1; minimum meter fill/track contrast is 3.49:1. Primary/secondary boundaries
and focus outlines meet 3:1. Keyboard focus remains in the dialog, welcome focus
is unclipped, and reduced motion suppresses dialog animation.

Controlled-response checks pass for all three hint choices, all auto-play modes
and stopping, normalized moves, trade opening/accept/reject/affordability and
protected dismissal, death/reset, and victory/next level. These use external CDP
interception of the original routes; there are no application mocks. The running
Java backend separately returns HTTP 200 for /state and renders a populated map
through the real frontend connection with no browser exceptions. The live read
check seeds the existing session flag to avoid requesting a game reset.

Taste lint reports zero failures and 15 warnings. Impeccable's detector completed
with font-choice warnings. The generic Taste inspector flags the game's terrain
sprites/directional buttons as icon chips and the intentional welcome overlay as
covered text. Gameplay probes retain requested warm-white, large-title,
compact-label, and map-as-empty-space heuristic warnings; there are no contrast
or overflow defects. The final screen uses two loaded fonts. These tool findings
are documented rather than presented as a blanket automated visual pass.

Evidence, external harnesses, captures, computed contrast, and hash manifests:
/tmp/wss-contrast/. Existing package and backend Maven metadata edits are kept;
no backend source or API configuration was changed.

## Compact workspace, shared modal headers, and gold indicator

- Scores moved into the 72px site header; the decorative hero row was removed.
  The board begins at y=130px on desktop, with map key and one-indexed coordinates
  in its caption. Board size follows viewport height and actual map aspect ratio.
- Desktop/tablet retain open side rails. Phone keeps natural page scrolling;
  content is not clipped or hidden to force a viewport fit.
- Brain, Vision, Guide, and death dialogs use a shared title/close header above
  independently scrolling content. Close targets are 44px, with visible focus.
  Existing focus trap, Escape eligibility, restoration, and handlers are retained.
- The resource strip places decorative yellow coins immediately after the gold
  number. Fill #EFC34E, outline #806000; Gold label and navy numeric text remain.
- npm build passes; lint has zero errors and the same seven existing hook warnings.
  git diff --check passes. Map.css checksum is unchanged, and all App code before
  the page render matches the prior source snapshot, including API/game handlers.
- Controlled browser checks confirm no page scroll at 1920x950, 1440x900,
  1366x768, 1280x720, 1024x768, 768x1024, and 768x768. Auto-play also fits at
  1366x768, 1280x720, and 768x768. Phone checks at 390x844 and 320x640 confirm no
  horizontal overflow. Final screenshots were opened at desktop, tablet, phone,
  and for modal headers and the guide's final scrolled entries.
- Modal titles and close buttons have matching vertical centers at 1440, 768,
  390, and 320px widths, with no overlap. Guide content remains reachable, dialog
  labels resolve to headings, Escape closes eligible dialogs, and movement still
  posts the original direction payload. No browser exceptions.
- Controlled gameplay checks pass for hints, autoplay and stop, trading, death and
  reset, victory and next level. All 55 existing computed contrast/state samples
  pass, including focus clipping, reduced motion, selected/disabled/error states.
  A separate unmocked browser session renders real backend state successfully.
- Independent GPT-6 Luna source audit found no definite regressions. Taste lint
  has zero failures and 15 inherited heuristic warnings. Generic Taste inspection
  sees the intentional welcome overlay as covered text and counts map sprites /
  movement controls as icon chips; its cream-palette, modal h1, and map-region
  heuristics are documented rather than treated as game-layout defects.
- Detailed evidence and external browser scripts: /tmp/wss-layout/complete,
  /tmp/wss-layout/gameplay, /tmp/wss-layout/states, and /tmp/wss-layout/inspect.
  Fixture routing exists only in external verification scripts, not app source.
