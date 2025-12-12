# FuzzyTrajectorySimilarity

Short description
-----------------
Code and materials for the M.Sc. thesis: **“A fuzzy method to measure the similarity of semantic trajectories”** (Faraz Boroomand, K.N. Toosi University of Technology, Winter 2021). This repository contains the implementation, data preparation and scripts used to develop and evaluate fuzzy-based trajectory similarity measures that account for uncertainty and semantic context.

Why this work
-------------
Trajectory similarity is central to many problems in movement analysis (clustering, anomaly detection, behaviour modelling). Standard measures (DTW, LCSS, EDR, Fréchet, Hausdorff, etc.) are sensitive to sampling rate and positional uncertainty. This thesis develops a fuzzy inference approach to (1) manage uncertainty when matching trajectory points, and (2) combine multiple contextual similarity aspects (e.g., spatial, semantic) into a single similarity score.

Key contributions
-----------------
- **FLCSS** — a fuzzy extension of Longest Common Subsequence that replaces hard thresholds with fuzzy membership for point matching, increasing robustness to noise and sampling-rate changes. In this method, instead of a binary “within-threshold” rule, distance between points is converted to a membership degree using fuzzy membership functions. This degree quantifies how well two points match.

<img src="/fuzzyPointMatching.jpg" width="500" height="400">

- **FTSM** — a composite fuzzy trajectory similarity measure that integrates point-level fuzzy matching, contextual features, and an aggregation rule to produce an overall similarity score. Detailed evaluation and sensitivity analysis are included in the thesis.
- Extensive experiments: synthetic and real trajectories, controlled noise, sampling-rate variation, and several baseline comparisons (LCSS, DTW, EDR, ERP, Fréchet, Hausdorff).

Data & experiments
------------------
- Synthetic trajectory generation (controlled perturbations: point displacement, sampling-rate changes, added noise). 
- Baseline comparison: LCSS, DTW, EDR, ERP, Fréchet, Hausdorff. Performance measured by robustness to noise and sampling changes.

Publications / related papers
----------------------------
- Boroumand, F., Alesheikh, A. A., Sharif, M., & Farnaghi, M. (2022). *FLCSS: A fuzzy‐based longest common subsequence method for uncertainty management in trajectory similarity measures*. Transactions in GIS, 26(5), 2244-2262. https://doi.org/10.1111/tgis.12958  
- Boroumand, F., Alesheikh, A. A., & Farnaghi, M. (2020). *Measuring the Similarity of Trajectories Using Fuzzy Theory*. Journal of Geomatics Science and Technology, 9(4), 131-143. **(in Persian)** http://jgst.issgeac.ir/article-1-891-en.html
